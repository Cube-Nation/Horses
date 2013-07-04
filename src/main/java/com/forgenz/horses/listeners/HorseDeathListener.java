/*
 * Copyright 2013 Michael McKnight. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */

package com.forgenz.horses.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import com.forgenz.forgecore.v1_0.bukkit.ForgeListener;
import com.forgenz.horses.Horses;
import com.forgenz.horses.PlayerHorse;
import com.forgenz.horses.config.HorseTypeConfig;
import com.forgenz.horses.config.HorsesConfig;

public class HorseDeathListener extends ForgeListener
{
	public HorseDeathListener(Horses plugin)
	{
		super(plugin);
		
		register();
	}
	
	@EventHandler
	public void onHorseDie(EntityDeathEvent event)
	{
		// We are only interested in horses which die
		if (event.getEntityType() != EntityType.HORSE)
		{
			return;
		}
		
		Horse horse = (Horse) event.getEntity();
		
		PlayerHorse horseData = PlayerHorse.getFromEntity(horse);
		
		HorsesConfig cfg = getPlugin().getHorsesConfig();
		
		if (cfg.deleteHorseOnDeath)
		{
			horseData.deleteHorse();
			return;
		}
		
		HorseTypeConfig typeCfg = cfg.getHorseTypeConfig(horseData.getType());
		
		horseData.removeHorse();
		
		boolean vip = Bukkit.getPlayerExact(horseData.getStable().getOwner()).hasPermission("horses.vip");
		
		double maxHealth = vip ? typeCfg.vipHorseMaxHp : typeCfg.defaultHorseMaxHp;
		double health = vip ? typeCfg.vipHorseHp : typeCfg.defaultHorseHp;
		
		horseData.setMaxHealth(maxHealth);
		horseData.setMaxHealth(health);
	}
	
	@Override
	public Horses getPlugin()
	{
		return (Horses) super.getPlugin();
	}
}