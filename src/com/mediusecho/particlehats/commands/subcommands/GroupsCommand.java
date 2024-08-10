package com.mediusecho.particlehats.commands.subcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import com.mediusecho.particlehats.ParticleHats;
import com.mediusecho.particlehats.commands.Command;
import com.mediusecho.particlehats.commands.Sender;
import com.mediusecho.particlehats.locale.Message;
import com.mediusecho.particlehats.permission.Permission;

public class GroupsCommand extends Command {
	
	public GroupsCommand ()
	{		
		register(new AddGroupCommand());
		register(new DeleteGroupCommand());
		register(new EditGroupCommand());
		register(new GroupInfoCommand());
	}
	
	@Override
	public boolean execute(ParticleHats core, Sender sender, String label, ArrayList<String> args) 
	{
		if (args.size() > 0)
		{	
			String argument = args.get(0);
			if (subCommands.containsKey(argument))
			{				
				args.remove(0);
				return subCommands.get(argument).onCommand(core, sender, label, args);
			}
		}
		return false;
	}
	
	@Override
	public List<String> tabComplete (ParticleHats core, Sender sender, String label, ArrayList<String> args)
	{
		if (args.size() == 1)
		{
			List<String> commands = new ArrayList<String>();
			for (Entry<String, Command> entry : subCommands.entrySet()) 
			{
				if (entry.getValue().hasPermission(sender)) {
					commands.add(entry.getKey());
				}
			}
			
			return commands;
		}
		
		else
		{
			String argument = args.get(0);
			if (subCommands.containsKey(argument))
			{
				Command subCommand = subCommands.get(argument);
				if (subCommand.hasPermission(sender))
				{
					args.remove(0);
					return subCommand.onTabComplete(core, sender, label, args);
				}
			}
		}
		return Arrays.asList("");
	}

	@Override
	public String getName() {
		return "group";
	}
	
	@Override
	public String getArgumentName () {
		return "group";
	}

	@Override
	public Message getUsage() {
		return Message.UNKNOWN;
	}

	@Override
	public Message getDescription() {
		return Message.UNKNOWN;
	}

	@Override
	public Permission getPermission() {
		return Permission.COMMAND_GROUP;
	}
	
	@Override
	public boolean hasPermission () {
		return false;
	}
	
	@Override
	public boolean hasWildcardPermission () {
		return true;
	}
	
	@Override
	public Permission getWildcardPermission () {
		return Permission.COMMAND_GROUP_ALL;
	}

	@Override
	public boolean showInHelp() {
		return false;
	}

	@Override
	public boolean isPlayerOnly() {
		return false;
	}

	
}
