package net.littlelite.jurpe.dungeon.generator;

/**
J.U.R.P.E. @version@ (DungeonCrawler Package)
Copyright (C) 2002-12 LittleLite Software
This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
import java.util.Random;

import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.RpgMapPoint;
import net.littlelite.jurpe.dungeon.rpgmap.Cell;
import net.littlelite.jurpe.dungeon.rpgmap.MapIterator;
import net.littlelite.jurpe.dungeon.rpgmap.RpgMap;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.Log;
import net.littlelite.jurpe.world.LocationType;

/**
 * Dungeon Generator class. Add Sparsify and MakeRooms to Mazifier
 * 
 * @see hexgenerator.Mazifier
 * 
 */
public class Dungeonifier extends Mazifier implements IDungeonGenerator
{

    /**
     * Constructor
     * 
     * @param x
     *            Width of dungeon
     * @param y
     *            Height of dungeon
     */
    public Dungeonifier(Random rnd, Log log, short x, short y)
    {
        super(rnd, log, x, y);
    }

    /**
     * Create a new dungeon
     * 
     * @return Newly created dungeon
     * @see hexgenerator.RpgMap
     */
    @Override
    public RpgMap createDungeon(int level) throws JurpeException
    {
        RpgMap dungeon = this.drawPerfectMaze();
        this.sparsifyMaze(dungeon, (short) 3);
        this.makeRooms(dungeon);
        this.closeEdges(dungeon);
        this.makeDoors(dungeon);
        this.addSpecialLocations(level, dungeon);
        this.resetVisited(dungeon);
        return dungeon;
    }

    /**
     * Add special locations. Special locations can be:
     * <ul>
     * <li>Stairs Up
     * <li>Stairs Down
     * <li>Signs
     * </ul>
     * 
     * @param level
     *            Level to add special locations to
     * @param logicMap
     *            Map to add special locations to
     */
    protected void addSpecialLocations(int level, RpgMap logicMap) throws JurpeException
    {
        RpgMap dungeonLevel = logicMap;

        Cell emptyCell;
        RpgMapPoint locationPoint;

        // adds from 0 to 2 stairs up
        int stairsUp = this.rndgen.nextInt(3);
        for (int j = 0; j <= stairsUp; j++)
        {
            emptyCell = dungeonLevel.getEmptyRandomCell(false);
            locationPoint = emptyCell.getCoordinates();
            dungeonLevel.setSpecialCell(locationPoint, LocationType.STAIRSUP);
        }
        log.addDetail("Added " + stairsUp + " stairs up.");

        // adds from 0 to 2 stairs down
        int stairsDown = this.rndgen.nextInt(4);
        for (int j = 0; j <= stairsDown; j++)
        {
            emptyCell = dungeonLevel.getEmptyRandomCell(false);
            locationPoint = emptyCell.getCoordinates();
            dungeonLevel.setSpecialCell(locationPoint, LocationType.STAIRSDOWN);
        }
        log.addDetail("Added " + stairsDown + " stairs down.");

        // adds one sign
        emptyCell = dungeonLevel.getEmptyRandomCell(false);
        locationPoint = emptyCell.getCoordinates();
        String sign = "The sign reads: 'Dungeon Level #" + String.valueOf(level) + "'";
        dungeonLevel.setSpecialCell(locationPoint, LocationType.SIGN, sign);
        log.addDetail("Added a strange sign.");

    }

    /**
     * Sparsify Maze. Deletes some branches of the perfect maze
     * 
     * @param rmap
     *            RpgMap to sparsify
     * @param sparseness
     *            Value between 0 (not sparse) and 5 (very sparse)
     */
    protected void sparsifyMaze(RpgMap rmap, short sparseness) throws JurpeException
    {
        Cell fromCell, toCell;
        short www = (short) (this.ww - 1);
        short hhh = (short) (this.hh - 1);

        this.log.addDetail("Sparsifying...");

        Direction d;

        for (short times = 0; times < sparseness; times++)
        {
            for (short x = 0; x < www; x++)
            {
                for (short y = 0; y < hhh; y++)
                {
                    fromCell = rmap.getCell(x, y);
                    if (fromCell.getNumberOfCorridors() == 1)
                    {
                        for (short j = 0; j < 6; j++)
                        {
                            d = Direction.fromValue(j);
                            if (fromCell.isCorridor(d))
                            {
                                fromCell.setCorridor(d, false);
                                toCell = rmap.getCell(fromCell, d);
                                toCell.setCorridor(Direction.getInverse(d), false);
                            }
                        }
                    }
                }
            }
        }
        this.log.addDetail("Sparsifying Finished");
    }

    protected void makeDoors(RpgMap rmap)
    {
        Cell cell;
        Cell adiacentCell;
        Random seed = this.rndgen;

        this.log.addDetail("Making doors...");

        MapIterator i = rmap.iterator();

        while (i.hasNext())
        {
            cell = i.next();
            if (!cell.isWall())
            {
                Direction[] doorables = cell.getDoorables();
                float fRand;

                if (doorables != null)
                {
                    for (Direction dd : doorables)
                    {
                        fRand = seed.nextFloat();
                        if (fRand < Config.DOORSPERCENTAGE)
                        {
                            adiacentCell = rmap.getCell(cell, dd);
                            if (adiacentCell != null)
                            {
                                if (!adiacentCell.isWall())
                                {
                                    cell.setDoor(dd);
                                    adiacentCell.setDoor(Direction.getInverse(dd));
                                }
                            }
                        }
                    }
                }
            }
        }

        this.log.addDetail("Making Doors Finished");

    }

    /**
     * Iterates through all the cells. If a cell is not a wall, then iterates
     * all its directions that are not opened corridors. If in a direction there
     * is no corridor, but the adiacent cell is not a wall, then open that
     * corridor.
     * 
     * @param rmap
     *            RpgMap to make rooms inside
     */
    protected void makeRooms(RpgMap rmap) throws JurpeException
    {
        Cell cell;
        Cell adiacentCell;
        Direction d;

        this.log.addDetail("Making rooms...");

        MapIterator i = rmap.iterator();

        while (i.hasNext())
        {
            cell = i.next();
            if (!cell.isWall())
            {
                for (short j = 0; j < 6; j++)
                {
                    d = Direction.fromValue(j);
                    if (!cell.isCorridor(d))
                    {
                        adiacentCell = rmap.getCell(cell, d);
                        if (adiacentCell == null)
                        {
                            cell.setWall();
                        }
                        else if (!adiacentCell.isWall())
                        {
                            cell.setCorridor(d, true);
                        }
                    }
                }
            }

        }

        this.log.addDetail("Making Rooms Finished");
    }
}
