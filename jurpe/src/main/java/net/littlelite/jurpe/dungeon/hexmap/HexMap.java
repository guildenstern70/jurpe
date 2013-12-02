package net.littlelite.jurpe.dungeon.hexmap;

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

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.AbstractList;

import javax.swing.JComponent;

import net.littlelite.jurpe.dungeon.Avatar;
import net.littlelite.jurpe.dungeon.Dungeons;
import net.littlelite.jurpe.dungeon.Placeholder;
import net.littlelite.jurpe.dungeon.crawler.Direction;
import net.littlelite.jurpe.dungeon.crawler.MapCrawler;
import net.littlelite.jurpe.dungeon.rpgmap.MapView;
import net.littlelite.jurpe.dungeon.rpgmap.RpgMap;
import net.littlelite.jurpe.system.Config;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.commands.DungeonCommand;

/**
 * HexMap is JComponent that has an hexagon map inside.
 */
public final class HexMap extends JComponent implements KeyListener
{

	private static final long serialVersionUID = 3321L;

	private Dungeons dungeon; // handle to rpgmap, avatar...
	private HexMapGeometry geometry;
	private MapCrawler crawler;
	private HexMapView hexMapView;
	private IPanelTemplate parent;

	private int hmWidth;
	private int hmHeight;

	/**
	 * Constructor
	 */
	protected HexMap(IPanelTemplate container)
	{
		this.parent = container;
		this.hexMapView = null;
		this.geometry = new HexMapGeometry(this);
		this.setDoubleBuffered(true);
		this.addKeyListener(this);
	}

	/**
	 * Get geometry
	 * 
	 * @see HexMapGeometry
	 * @return geometry
	 */
	public HexMapGeometry getGeometry()
	{
		return this.geometry;
	}

	/**
	 * Get dungeon handles
	 * 
	 * @return Dungeon
	 */
	public Dungeons getDungeon()
	{
		return this.dungeon;
	}

	/**
	 * Get map width in number of hexagons. The number is comprehensive of the
	 * two half hexagons displayed left and right.
	 * 
	 * @return map width in number of hexagons
	 */
	public short getWidthInHexagons()
	{
		short w = (short) ((this.getWidth() / Config.HEXAGON_WIDTH) + 1);
		return w;
	}

	public int getWidth()
	{
		if (this.hmWidth > 0)
		{
			return this.hmWidth;
		}

		return super.getWidth();
	}

	public int getHeight()
	{
		if (this.hmHeight > 0)
		{
			return this.hmHeight;
		}

		return super.getHeight();
	}

	/**
	 * Set the component size, with additional logic to keep these numbers
	 * fixed.
	 */
	public void setSize(int width, int height)
	{
		this.hmWidth = width;
		this.hmHeight = height;
		super.setSize(width, height);
	}

	/**
	 * Get map height in number of hexagons. The number is comprehensive of the
	 * two half hexagons displayed up and down.
	 * 
	 * @return map height in number of hexagons
	 */
	public short getHeightInHexagons()
	{
		short h = (short) ((this.getHeight() / Config.HEXAGON_HEIGHT) + 1);
		return h;
	}

	/**
	 * HexMapView handle (RpgMap and HexMap methods)
	 * 
	 * @return HexMapView
	 */
	public HexMapView getHexMapView() throws JurpeException
	{
		if (this.hexMapView == null)
		{
			throw new JurpeException("Trying to use HexMapView before having initilized it. Call synchHexMapView() before.");
		}
		return this.hexMapView;
	}

	/**
	 * Synchronize HexMapView with a RpgMap
	 * 
	 */
	public void synchHexMapView() throws JurpeException
	{
		RpgMap map = this.dungeon.getCurrentMap();
		if (map == null)
		{
			this.dungeon.initializeLevels();
		}
		this.hexMapView = new HexMapView(this, this.dungeon.getCurrentMap());
	}

	/**
	 * The Game handle
	 * 
	 * @param game
	 *            Handle to Game
	 */
	public void setDungeon(Dungeons game)
	{
		this.dungeon = game;
		this.crawler = MapCrawler.getReference(game);
	}

	/**
	 * Render background according to RpgMap
	 */
	public void renderBackground(Graphics g) throws JurpeException
	{
		this.refresh(g);
		this.requestFocus();
		g.dispose(); // THIS ENDS UP GRAPHIC PAINTING OF HEXMAP
	}

	/**
	 * Paint procedure
	 * 
	 * @param g
	 */
	protected void paintComponent(Graphics g)
	{
		try
		{
			super.paintComponent(g);
			this.renderBackground(g);
		}
		catch (JurpeException je)
		{
			System.err.println(je.getMessage());
			je.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Handle key press event
	 */
	public void keyPressed(KeyEvent ke)
	{
		try
		{
			DungeonCommand command = this.crawler.recognizeCommand(ke);
			if (command != null)
			{
				this.processCommand(command);
			}
		}
		catch (JurpeException jex)
		{
			jex.printStackTrace();
		}
	}

	private void processCommand(DungeonCommand command) throws JurpeException
	{
		// Move all placeholders
		this.dungeon.moveMonsters();
		AbstractList<Placeholder> attMonsters = this.dungeon.getAttackingMonsters();

		if (attMonsters != null)
		{
			for (Placeholder p : attMonsters)
			{
				this.parent.getGUI().executeAttackByAMonster(p.getName());
			}
		}

		if (command.getFeedback() != null)
		{
			this.dungeon.getLog().addEntry(command.getFeedback());

			// Execute command
			this.parent.getGUI().executeDungeonCommand(command);
			this.parent.doLayout(); // refresh log
		}

		this.refresh();
	}

	/**
	 * If it's focusable
	 * 
	 * @return Yes it is
	 */
	public boolean isFocusable()
	{
		return true;
	}

	public void keyReleased(KeyEvent ke)
	{
	}

	public void keyTyped(KeyEvent ke)
	{
	}

	/**
	 * Refresh PlaceHolder in view map after any command, scrolling view if
	 * necessary
	 */
	public void refresh() throws JurpeException
	{
		Graphics g = this.getGraphics();
		if (g != null)
		{
			this.refresh(g);
			g.dispose();
		}
	}

	private void refresh(Graphics g) throws JurpeException
	{
		Avatar av = this.dungeon.getAvatar();
		if (av != null)
		{
			Direction edge = av.getEdge(this);
			if (edge != null) // is Avatar on the edge? If so, scroll map
			{
				HexMapView hMap = this.getHexMapView();
				MapView view = hMap.getMapView();
				view.scrollView(av.getPlaceholder().getPosition(), edge);
			}
			this.geometry.drawing.drawMaze(g, av);
			this.geometry.drawing.drawPlaceholders(g, this.dungeon);
		}
	}

}
