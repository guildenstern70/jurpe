/**
 * J.U.R.P.E.
 * 
 * @version@ (System Package) Copyright (C) 2002-12 LittleLite Software
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.littlelite.jurpe.system.commands.player;

public class CommandFlags
{

	private boolean aim; // system switch to aim mode
	private boolean dig; // system expect a direction to dig
	private boolean open; // system expect a direction to open
	private boolean close; // system expect a direction to close
	private boolean monsterAttack; // if set to true, then a monster

	// attacks player
	public boolean isAim()
	{
		return aim;
	}

	public void setAim(boolean aim)
	{
		this.aim = aim;
	}

	public boolean isDig()
	{
		return dig;
	}

	public void setDig(boolean dig)
	{
		this.dig = dig;
	}

	public boolean isOpen()
	{
		return open;
	}

	public void setOpen(boolean open)
	{
		this.open = open;
	}

	public boolean isClose()
	{
		return close;
	}

	public void setClose(boolean close)
	{
		this.close = close;
	}

	public boolean isMonsterAttack()
	{
		return monsterAttack;
	}

	public void setMonsterAttack(boolean monsterAttack)
	{
		this.monsterAttack = monsterAttack;
	}
}
