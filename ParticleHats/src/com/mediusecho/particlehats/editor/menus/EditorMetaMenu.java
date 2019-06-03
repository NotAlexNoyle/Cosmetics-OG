package com.mediusecho.particlehats.editor.menus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mediusecho.particlehats.ParticleHats;
import com.mediusecho.particlehats.compatibility.CompatibleMaterial;
import com.mediusecho.particlehats.database.Database;
import com.mediusecho.particlehats.database.Database.DataType;
import com.mediusecho.particlehats.editor.EditorLore;
import com.mediusecho.particlehats.editor.EditorMenu;
import com.mediusecho.particlehats.editor.MenuBuilder;
import com.mediusecho.particlehats.editor.MetaState;
import com.mediusecho.particlehats.locale.Message;
import com.mediusecho.particlehats.particles.Hat;
import com.mediusecho.particlehats.util.ItemUtil;

public class EditorMetaMenu extends EditorMenu {

	private final Hat targetHat;
	
	public EditorMetaMenu(ParticleHats core, Player owner, MenuBuilder menuBuilder) 
	{
		super(core, owner, menuBuilder);
		
		targetHat = menuBuilder.getBaseHat();
	
		inventory = Bukkit.createInventory(null, 54, Message.EDITOR_META_MENU_TITLE.getValue());
		build();
	}
	
	/**
	 * Update all of our meta properties when they open the menu since these properties are changed outside the editor
	 */
	@Override
	public void open ()
	{
		EditorLore.updateNameDescription(getItem(13), targetHat);	
		EditorLore.updateDescriptionDescription(getItem(11), targetHat.getDescription());
		EditorLore.updateDescriptionDescription(getItem(19), targetHat.getPermissionDescription());
		EditorLore.updatePermissionDescription(getItem(15), targetHat);
		EditorLore.updateLabelDescription(getItem(29), targetHat.getLabel());
		EditorLore.updateEquipDescription(getItem(33), targetHat.getEquipDisplayMessage());
		EditorLore.updatePermissionDeniedDescription(getItem(25), targetHat.getPermissionDeniedDisplayMessage());
		
		super.open();
	}

	@Override
	protected void build() 
	{
		setButton(49, backButton, backAction);
		
		// Name
		ItemStack nameItem = ItemUtil.createItem(CompatibleMaterial.PLAYER_HEAD, Message.EDITOR_META_MENU_SET_NAME);
		setButton(13, nameItem, (event, slot) ->
		{
			if (event.isLeftClick())
			{
				menuBuilder.setOwnerState(MetaState.HAT_NAME);
				core.prompt(owner, MetaState.HAT_NAME);
				owner.closeInventory();
			}
			
			else if (event.isRightClick())
			{
				targetHat.setName(Message.EDITOR_MISC_NEW_PARTICLE.getRawValue());
				EditorLore.updateNameDescription(getItem(13), targetHat);
			}
			
			return EditorClickType.NEUTRAL;
		});
		
		// Description
		ItemStack descriptionItem = ItemUtil.createItem(CompatibleMaterial.WRITABLE_BOOK, Message.EDITOR_META_MENU_SET_DESCRIPTION);
		setButton(11, descriptionItem, (event, slot) ->
		{
			if (event.isLeftClick())
			{
				EditorDescriptionMenu editorDescriptionMenu = new EditorDescriptionMenu(core, owner, menuBuilder, true);
				menuBuilder.addMenu(editorDescriptionMenu);
				editorDescriptionMenu.open();
			}
			
			else if (event.isShiftRightClick())
			{
				if (!targetHat.getDescription().isEmpty())
				{
					targetHat.getDescription().clear();
					
					Database database = core.getDatabase();
					String menuName = menuBuilder.getEditingMenu().getName();
					database.saveMetaData(menuName, targetHat, DataType.DESCRIPTION, 0);
					
					EditorLore.updateDescriptionDescription(getItem(11), targetHat.getDescription());
				}
			}
			return EditorClickType.NEUTRAL;
		});
		
		// Permission Description
		ItemStack permissionDescriptionItem = ItemUtil.createItem(Material.BOOK, Message.EDITOR_META_MENU_SET_PERMISSION_DESCRIPTION);
		setButton(19, permissionDescriptionItem, (event, slot) ->
		{
			if (event.isLeftClick())
			{
				EditorDescriptionMenu editorDescriptionMenu = new EditorDescriptionMenu(core, owner, menuBuilder, false);
				menuBuilder.addMenu(editorDescriptionMenu);
				editorDescriptionMenu.open();
			}
			
			else if (event.isShiftRightClick())
			{
				if (!targetHat.getPermissionDescription().isEmpty())
				{
					targetHat.getPermissionDescription().clear();
					
					Database database = core.getDatabase();
					String menuName = menuBuilder.getEditingMenu().getName();
					database.saveMetaData(menuName, targetHat, DataType.PERMISSION_DESCRIPTION, 0);
					
					EditorLore.updateDescriptionDescription(getItem(19), targetHat.getPermissionDescription());
				}
			}
			return EditorClickType.NEUTRAL;
		});
		
		// Permission
		ItemStack permissionItem = ItemUtil.createItem(Material.PAPER, Message.EDITOR_META_MENU_SET_PERMISSION);
		setButton(15, permissionItem, (event, slot) ->
		{
			menuBuilder.setOwnerState(MetaState.HAT_PERMISSION);
			core.prompt(owner, MetaState.HAT_PERMISSION);
			owner.closeInventory();
			return EditorClickType.NEUTRAL;
		});
		
		// Label
		ItemStack labelItem = ItemUtil.createItem(Material.NAME_TAG, Message.EDITOR_META_MENU_SET_LABEL);
		setButton(29, labelItem, (event, slot) ->
		{
			if (event.isLeftClick())
			{
				menuBuilder.setOwnerState(MetaState.HAT_LABEL);
				core.prompt(owner, MetaState.HAT_LABEL);
				owner.closeInventory();
			}
			
			else if (event.isShiftRightClick())
			{
				core.getDatabase().onLabelChange(targetHat.getLabel(), null, null, -1);
				targetHat.removeLabel();
				EditorLore.updateLabelDescription(getItem(29), targetHat.getLabel());
			}
			return EditorClickType.NEUTRAL;
		});
		
		// Equip
		ItemStack equipItem = ItemUtil.createItem(Material.LEATHER_HELMET, Message.EDITOR_META_MENU_SET_EQUIP_MESSAGE);
		setButton(33, equipItem, (event, slot) ->
		{
			if (event.isLeftClick())
			{
				menuBuilder.setOwnerState(MetaState.HAT_EQUIP_MESSAGE);
				core.prompt(owner, MetaState.HAT_EQUIP_MESSAGE);
				owner.closeInventory();
			}
			
			else if (event.isShiftRightClick())
			{
				targetHat.removeEquipMessage();
				EditorLore.updateEquipDescription(getItem(33), targetHat.getEquipDisplayMessage());
			}
			return EditorClickType.NEUTRAL;
		});
		
		// Permission Denied
		ItemStack permissionDeniedItem = ItemUtil.createItem(CompatibleMaterial.MAP, Message.EDITOR_META_MENU_SET_PERMISSION_MESSAGE);
		setButton(25, permissionDeniedItem, (event, slot) ->
		{
			if (event.isLeftClick())
			{
				menuBuilder.setOwnerState(MetaState.HAT_PERMISSION_MESSAGE);
				core.prompt(owner, MetaState.HAT_PERMISSION_MESSAGE);
				owner.closeInventory();
			}
			
			else if (event.isShiftRightClick())
			{
				targetHat.removePermissionDeniedMessage();
				EditorLore.updatePermissionDeniedDescription(getItem(25), targetHat.getPermissionDeniedDisplayMessage());
			}
			return EditorClickType.NEUTRAL;
		});
		
		// Tags
		ItemStack tagItem = ItemUtil.createItem(Material.BOWL, Message.EDITOR_META_MENU_SET_TAG);
		setButton(31, tagItem, (event, slot) ->
		{
			EditorTagOverviewMenu editorTagOverviewMenu = new EditorTagOverviewMenu(core, owner, menuBuilder);
			menuBuilder.addMenu(editorTagOverviewMenu);
			editorTagOverviewMenu.open();
			return EditorClickType.NEUTRAL;
		});
	}

}
