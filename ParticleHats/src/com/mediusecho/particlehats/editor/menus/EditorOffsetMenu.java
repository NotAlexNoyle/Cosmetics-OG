package com.mediusecho.particlehats.editor.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.mediusecho.particlehats.Core;
import com.mediusecho.particlehats.editor.EditorLore;
import com.mediusecho.particlehats.editor.EditorMenu;
import com.mediusecho.particlehats.editor.MenuBuilder;
import com.mediusecho.particlehats.locale.Message;
import com.mediusecho.particlehats.particles.Hat;
import com.mediusecho.particlehats.util.ItemUtil;

public class EditorOffsetMenu extends EditorMenu {

	protected final EditorGenericCallback callback;
	
	public EditorOffsetMenu(Core core, Player owner, MenuBuilder menuBuilder, EditorGenericCallback callback)
	{
		super(core, owner, menuBuilder);
		this.callback = callback;
		
		inventory = Bukkit.createInventory(null, 45, Message.EDITOR_OFFSET_MENU_TITLE.getValue());
		build();
	}
	
	@Override
	public void onClose (boolean forced)
	{
		if (!forced) {
			callback.onExecute();
		}
	}

	private EditorClickType updateOffset (EditorClickEvent event, Hat hat, VectorAxis axis, boolean random)
	{
		double normalClick    = event.isLeftClick() ? 0.1f : -0.1f;
		double shiftClick     = event.isShiftClick() ? 10 : 1;
		double modifier       = normalClick * shiftClick;
		boolean isMiddleClick = event.isMiddleClick();
		
		Vector offset = random ? hat.getRandomOffset() : hat.getOffset();
		int xslot = random ? 32 : 14;
		int yslot = random ? 33 : 15;
		int zslot = random ? 34 : 16;
		
		switch (axis)
		{
			case X:
			{
				double xo = !isMiddleClick ? offset.getX() + modifier : 0;
				if (random) {
					hat.setRandomOffsetX(xo);
				} else {
					hat.setOffsetX(xo);
				}
				break;
			}
			
			case Y:
			{
				double yo = !isMiddleClick ? offset.getY() + modifier : 0;
				if (random) {
					hat.setRandomOffsetY(yo);
				} else {
					hat.setOffsetY(yo);
				}
				break;
			}
			
			case Z:
			{
				double zo = !isMiddleClick ? offset.getZ() + modifier : 0;
				if (random) {
					hat.setRandomOffsetZ(zo);
				} else {
					hat.setOffsetZ(zo);
				}
				break;
			}
		}

		EditorLore.updateVectorDescription(getItem(xslot), offset, Message.EDITOR_OFFSET_MENU_OFFSET_X_DESCRIPTION);
		EditorLore.updateVectorDescription(getItem(yslot), offset, Message.EDITOR_OFFSET_MENU_OFFSET_Y_DESCRIPTION);
		EditorLore.updateVectorDescription(getItem(zslot), offset, Message.EDITOR_OFFSET_MENU_OFFSET_Z_DESCRIPTION);
		
		if (event.isMiddleClick()) {
			return EditorClickType.NEUTRAL;
		}
		
		else {
			return event.isLeftClick() ? EditorClickType.POSITIVE : EditorClickType.NEGATIVE;
		}
	}
	
	@Override
	protected void build() 
	{
		Hat targetHat = menuBuilder.getTargetHat();
		
		// X Offset
		ItemStack xItem = ItemUtil.createItem(Material.REPEATER, Message.EDITOR_OFFSET_MENU_SET_OFFSET_X);
		EditorLore.updateVectorDescription(xItem, targetHat.getOffset(), Message.EDITOR_OFFSET_MENU_OFFSET_X_DESCRIPTION);
		setButton(14, xItem, (event, slot) ->
		{
			return updateOffset(event, targetHat, VectorAxis.X, false);
		});
		
		// Y Offset
		ItemStack yItem = ItemUtil.createItem(Material.REPEATER, Message.EDITOR_OFFSET_MENU_SET_OFFSET_Y);
		EditorLore.updateVectorDescription(yItem, targetHat.getOffset(), Message.EDITOR_OFFSET_MENU_OFFSET_Y_DESCRIPTION);
		setButton(15, yItem, (event, slot) ->
		{
			return updateOffset(event, targetHat, VectorAxis.Y, false);
		});
		
		// Z Offset
		ItemStack zItem = ItemUtil.createItem(Material.REPEATER, Message.EDITOR_OFFSET_MENU_SET_OFFSET_Z);
		EditorLore.updateVectorDescription(zItem, targetHat.getOffset(), Message.EDITOR_OFFSET_MENU_OFFSET_Z_DESCRIPTION);
		setButton(16, zItem, (event, slot) ->
		{
			return updateOffset(event, targetHat, VectorAxis.Z, false);
		});
		
		// Random X Offset
		ItemStack rxItem = ItemUtil.createItem(Material.COMPARATOR, Message.EDITOR_OFFSET_MENU_SET_RANDOM_OFFSET_X);
		EditorLore.updateVectorDescription(rxItem, targetHat.getRandomOffset(), Message.EDITOR_OFFSET_MENU_OFFSET_X_DESCRIPTION);
		setButton(32, rxItem, (event, slot) ->
		{
			return updateOffset(event, targetHat, VectorAxis.X, true);
		});
		
		// Random Y Offset
		ItemStack ryItem = ItemUtil.createItem(Material.COMPARATOR, Message.EDITOR_OFFSET_MENU_SET_RANDOM_OFFSET_Y);
		EditorLore.updateVectorDescription(ryItem, targetHat.getRandomOffset(), Message.EDITOR_OFFSET_MENU_OFFSET_Y_DESCRIPTION);
		setButton(33, ryItem, (event, slot) ->
		{
			return updateOffset(event, targetHat, VectorAxis.Y, true);
		});
		
		// Random Z Offset
		ItemStack rzItem = ItemUtil.createItem(Material.COMPARATOR, Message.EDITOR_OFFSET_MENU_SET_RANDOM_OFFSET_Z);
		EditorLore.updateVectorDescription(rzItem, targetHat.getRandomOffset(), Message.EDITOR_OFFSET_MENU_OFFSET_Z_DESCRIPTION);
		setButton(34, rzItem, (event, slot) ->
		{
			return updateOffset(event, targetHat, VectorAxis.Z, true);
		});
		
		// Back
		setButton(19, backButton, backAction);
	}
	
	protected enum VectorAxis 
	{	
		X,
		Y,
		Z;
	}
}
