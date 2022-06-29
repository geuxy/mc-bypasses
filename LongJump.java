package snooze.cheat.cheats.movement;

import java.util.ArrayList;

import snooze.cheat.Cheat;
import snooze.cheat.EnumCategory;
import snooze.event.Event;
import snooze.event.events.EventUpdate;
import snooze.settings.Setting;

public class LongJump extends Cheat {
	private ArrayList<String> mode;
	private boolean jumped;
	private double moveSpeed;
	
	public LongJump() {
		super("LongJump", EnumCategory.MOVEMENT);
		this.mode = new ArrayList();
		this.mode.add("Motion");
		this.mode.add("NCP");
		this.addSetting(new Setting("LongJump-Mode", this, "Motion", mode));
		this.addSetting(new Setting("Speed", this, 1.6, 0.2, 10, false));
		this.addSetting(new Setting("Slowdown Speed", this, 0.02, 0.01, 0.2, false));
		this.addSetting(new Setting("Height", this, 1.6, 0.2, 10, false));
		this.addSetting(new Setting("Auto Disable", this, false));

	}
	
	@Override
	public void onEnabled() {
		this.moveSpeed = this.getSetting("Speed", this).getValDouble();
		super.onEnabled();
	}
	
	@Override
	public void onDisabled() {
		jumped = false;
		moveSpeed = 0;
		super.onDisabled();
	}
	
	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
			if(e.isPre()) {
				switch(this.getSetting("LongJump-Mode", this).getValString()) {
					case "Motion": {
						if(mc.thePlayer.onGround) {
							this.setMoveSpeed(0);
							if(jumped && this.getSetting("Auto Disable", this).getValBoolean()) {
								this.setEnabled(false);
							} else {
								mc.thePlayer.jump();
								mc.thePlayer.motionY *= this.getSetting("Height", this).getValDouble();
							}
						} else {
							jumped = true;
							this.setMoveSpeed(this.getSetting("Speed", this).getValDouble());
						}
						break;
					}
					case "NCP": {
						if(mc.thePlayer.onGround) {
							if(jumped && this.getSetting("Auto Disable", this).getValBoolean()) {
								this.setMoveSpeed(0);
								this.setEnabled(false);
							} else {
								if(!this.getSetting("Auto Disable", this).getValBoolean()) {
									this.moveSpeed = this.getSetting("Speed", this).getValDouble();
								}
								mc.thePlayer.jump();
							}
						} else {
							jumped = true;
							if(moveSpeed < 0.2) {
								moveSpeed = 0.2;
							} else {
								moveSpeed -= this.getSetting("Slowdown Speed", this).getValDouble();
							}
							this.setMoveSpeed(moveSpeed);
						}
						break;
					}
				}
			}
		}
		super.onEvent(e);
	}

}
