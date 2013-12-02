/**
J.U.R.P.E. @version@ Swing Demo
Copyright (C) LittleLite Software
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
package net.littlelite.jurpedemo.frames.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import net.littlelite.jurpe.characters.AbstractPC;
import net.littlelite.jurpe.items.Armor;
import net.littlelite.jurpe.items.AbstractItem;
import net.littlelite.jurpe.items.Item;
import net.littlelite.jurpe.items.Shield;
import net.littlelite.jurpe.items.Weapon;
import net.littlelite.jurpe.system.JurpeException;
import net.littlelite.jurpe.system.resources.ResourceFinder;
import net.littlelite.jurpe.world.Shop;
import net.littlelite.jurpedemo.frames.JurpeMain;
import net.littlelite.jurpedemo.frames.PopupListener;
import net.littlelite.jurpedemo.resources.GameStrings;

public final class ShopPanel extends JPnlShop
{
    // UID
    private static final long serialVersionUID = 3L;
    private Shop shop;
    private AbstractPC visitingCharacter;

    /**
     * Construct a Shop panel
     * @param gui
     */
    public ShopPanel(JurpeMain gui) throws JurpeException
    {
        super(gui);
    }
    
    @Override
    protected void formComponentShown(java.awt.event.ComponentEvent evt)
    {
        this.setShopCatalogue();
        this.parentGUI.getSystem().getLog().addDetail("Shop built");
    }

    private void setShopCatalogue()
    {
        this.shop = this.parentGUI.getSystem().getShop();
        this.jTree = this.buildTree();
        this.jScrollPane.setViewportView(this.jTree);
    }

    private JTree buildTree()
    {
        JTree newTree = null;

        if (this.shop != null)
        {
            this.top = new DefaultMutableTreeNode();
            top.setUserObject(this.shop.getName());
            this.createShopNodes(top);
            newTree = new JTree(top);
            ToolTipManager.sharedInstance().registerComponent(newTree);
            newTree.setCellRenderer(new ShopTreeRenderer());
            this.initPopupMenu(newTree);
        }

        return newTree;
    }

    /**
     * Create nodes for hierarchical tree in the shop panel.
     * 
     * @param topNode
     *            TreeNode top note (ie: Shop)
     */
    private void createShopNodes(DefaultMutableTreeNode topNode)
    {
        this.visitingCharacter = this.parentGUI.getSystem().getPC();
        if (this.visitingCharacter != null)
        {
            this.parentGUI.getSystem().getLog().addDetail(this.visitingCharacter.getShortDescription() + " is visiting the shop.");
        }

        DefaultMutableTreeNode weaponsTreeNode = new DefaultMutableTreeNode(GameStrings.WEAPONS);
        DefaultMutableTreeNode armorsTreeNode = new DefaultMutableTreeNode(GameStrings.ARMORS);
        DefaultMutableTreeNode shieldsTreeNode = new DefaultMutableTreeNode(GameStrings.SHIELDS);
        DefaultMutableTreeNode itemsTreeNode = new DefaultMutableTreeNode(GameStrings.ITEMS);

        topNode.add(weaponsTreeNode);
        topNode.add(armorsTreeNode);
        topNode.add(shieldsTreeNode);
        topNode.add(itemsTreeNode);

        Iterator<Weapon> iW = this.shop.getWeapons().iterator();
        while (iW.hasNext())
        {
            weaponsTreeNode.add(new DefaultMutableTreeNode(iW.next(), false));
        }

        Iterator<Armor> iA = this.shop.getArmors().iterator();
        while (iA.hasNext())
        {
            armorsTreeNode.add(new DefaultMutableTreeNode(iA.next(), false));
        }

        Iterator<Shield> iS = this.shop.getShields().iterator();
        while (iS.hasNext())
        {
            shieldsTreeNode.add(new DefaultMutableTreeNode(iS.next(), false));
        }

        Iterator<Item> iI = this.shop.getItems().iterator();
        while (iI.hasNext())
        {
            itemsTreeNode.add(new DefaultMutableTreeNode(iI.next(), false));
        }

    }

    private void initPopupMenu(JTree parentTree)
    {
        if (parentTree == null)
        {
            return;
        }

        JMenuItem miBuy = new JMenuItem(GameStrings.BUY);
        JMenuItem miExamine = new JMenuItem(GameStrings.EXAMINE);
        JPopupMenu popup = new JPopupMenu(GameStrings.SHOP);
        ActionListener al = new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                String command = e.getActionCommand();
                if (command.equals(GameStrings.BUY)) // Buy Item
                {
                    buyItem();
                }
                else if (command.equals(GameStrings.EXAMINE))
                {
                    examineItem();
                }
            }
        };
        miBuy.addActionListener(al);
        miExamine.addActionListener(al);
        popup.add(miBuy);
        popup.add(miExamine);
        parentTree.addMouseListener(new PopupListener(popup));
        parentTree.add(popup);
    }

    private AbstractItem getSelectedItem()
    {
        AbstractItem selBasicItem = null;

        TreePath tp = this.jTree.getSelectionPath();
        if (tp != null)
        {
            DefaultMutableTreeNode selNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
            if (selNode.isLeaf())
            {
                selBasicItem = (AbstractItem) selNode.getUserObject();
            }
        }

        return selBasicItem;
    }

    private void buyItem()
    {
        AbstractItem selBasicItem = this.getSelectedItem();
        if (selBasicItem != null)
        {
            this.parentGUI.buyItem(selBasicItem);
        }
    }

    private void examineItem()
    {
        AbstractItem selBasicItem = this.getSelectedItem();
        if (selBasicItem != null)
        {
            this.parentGUI.examineItem(selBasicItem);
        }
    }

    final class ShopTreeRenderer extends DefaultTreeCellRenderer
    {

        private static final long serialVersionUID = ShopPanel.serialVersionUID;
        private ImageIcon shieldIcon;
        private ImageIcon potionIcon;
        private ImageIcon armorIcon;
        private ImageIcon weaponIcon;

        public ShopTreeRenderer()
        {
            ResourceFinder rf = ResourceFinder.resources();
            shieldIcon = rf.getResourceAsImage("shield.gif");
            potionIcon = rf.getResourceAsImage("potion.gif");
            armorIcon = rf.getResourceAsImage("armor.gif");
            weaponIcon = rf.getResourceAsImage("sword.gif");
        }

        /* (non-Javadoc)
         * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
         */
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasTheFocus)
        {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasTheFocus);

            if (leaf)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                AbstractItem bi = (AbstractItem) (node.getUserObject());
                String tooltip = String.valueOf(bi.getWeight()) + " kg";
                setIcon(this.getCustIcon(bi));
                if ((visitingCharacter != null) && (!bi.canBeWorn(visitingCharacter)))
                {
                    this.setForeground(Color.RED);
                    tooltip += GameStrings.CANNOTUSE;
                }
                if (bi.getCost() > pcMoney)
                {
                    this.setForeground(Color.GRAY);
                    tooltip += GameStrings.CANNOTAFFORD;
                }
                this.setText(bi.toStringWCost());
                setToolTipText(tooltip);
            }
            else
            {
                setToolTipText(null); // no tool tip
            }

            return this;
        }

        private ImageIcon getCustIcon(AbstractItem value)
        {

            AbstractItem bi = value;
            ImageIcon icon = null;

            switch (bi.getType())
            {
                case ARMOR:
                    icon = armorIcon;
                    break;

                case SHIELD:
                    icon = shieldIcon;
                    break;

                case WEAPON:
                    icon = weaponIcon;
                    break;

                case ITEM:
                default:
                    icon = potionIcon;
                    break;
            }

            return icon;
        }
    }
}
