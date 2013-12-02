package net.littlelite.jurpe.system.generation;

/**
 J.U.R.P.E. @version@
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

import net.littlelite.utils.AxsRand;

/**
 * This class generates costs for randomly created items.
 */
public final class CostGenerator
{

	private int basicCost; // min cost for the item

	private int upperModifier;

	// parameter that exponentially affects cost (ie: IQ + 2, 2 is
	// upperModifier)
	private double upperModifierWeight; // weight for upper Modifier

	private int lowerModifier;

	// parameter that lowers the cost (IE: in armors, their weight)
	private int lowerModifierWeight; // weight for lower Modifier

	private int cCost; // Computed Cost

	private int cReduction;

	// Computed Reduction on cost (based on lowermodifier)

	/**
	 * Constructor. Commonly used parameters are: 50,x,2 (Random Items) where x
	 * is modifier
	 * 
	 * @param bc
	 *            Basic Cost for item (minimum value)
	 * @param um
	 *            Parameter that exponentially affects cost (ie: IQ + 2, 2 is
	 *            upperModifier)
	 * @param lm
	 *            Parameter that lowers the cost (IE: in armors, their weight)
	 */
	public CostGenerator(int bc, int um, int lm)
	{
		AxsRand axs = AxsRand.getReference();
		this.basicCost = bc + axs.randInt(bc);
		this.upperModifier = um;
		this.lowerModifier = lm;
		this.upperModifierWeight = 12;
		this.lowerModifierWeight = 2;
	}

	/**
	 * Constructor
	 * 
	 * @param bc
	 *            Basic Cost for item (minimum value)
	 * @param um
	 *            Parameter that exponentially affects cost (ie: IQ + 2, 2 is
	 *            upperModifier)
	 * @param umw
	 *            Weight for upper Modifier
	 * @param lm
	 *            Parameter that lowers the cost (IE: in armors, their weight)
	 * @param lmw
	 *            Weight for lower Modifier
	 */
	public CostGenerator(int bc, int um, int lm, double umw, int lmw)
	{
		AxsRand axs = AxsRand.getReference();
		this.basicCost = bc + axs.randInt(bc);
		this.upperModifier = um;
		this.upperModifierWeight = umw;
		this.lowerModifier = lm;
		this.lowerModifierWeight = lmw;
	}

	/**
	 * Set upper modifier
	 * 
	 * @param um
	 *            UpperModifier
	 */
	public void setUpperModifier(int um)
	{
		this.upperModifier = um;
	}

	/**
	 * Set lower modifier
	 * 
	 * @param lm
	 *            UpperModifier
	 */
	public void setLowerModifier(int lm)
	{
		this.lowerModifier = lm;
	}

	/**
	 * Get the cost
	 * 
	 * @return Computed cost
	 */
	public int getCost()
	{
		this.cCost = this.basicCost + (int) Math.round(this.upperModifierWeight * Math.pow(this.upperModifier, 2));
		this.cReduction = Math.min(0, (int) Math.round(-this.lowerModifierWeight * Math.pow(this.lowerModifier, 2)));
		return Math.max(this.basicCost, this.cCost + this.cReduction);
	}

	/**
	 * Get reduction
	 * 
	 * @return Cost reduction
	 */
	public int getReduction()
	{
		return Math.min(0, this.cReduction);
	}

	public static void main(String[] args)
	{
		CostGenerator cg = new CostGenerator(50, 1 / 2, 2);
		for (int j = 0; j <= 30; j++)
		{
			cg.setUpperModifier(j);
			System.out.print(j);
			System.out.print("> ");
			System.out.print(cg.getCost());
			System.out.print(" (");
			System.out.print(cg.getReduction());
			System.out.println(")");
		}

	}
}
