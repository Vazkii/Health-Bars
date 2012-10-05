package vazkii.healthbars.client;

import java.io.File;
import java.util.EnumSet;

import net.minecraft.src.KeyBinding;
import net.minecraft.src.NBTTagCompound;

import org.lwjgl.input.Keyboard;

import vazkii.codebase.client.ClientUtils;
import vazkii.codebase.common.CommonUtils;
import vazkii.codebase.common.EnumVazkiiMods;
import vazkii.codebase.common.IOUtils;
import vazkii.healthbars.common.mod_HealthBars;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class HealthBarsKeyHandler extends KeyHandler {

	public static KeyBinding key = new KeyBinding("Health Bars", Keyboard.KEY_B);

	public HealthBarsKeyHandler() {
		super(new KeyBinding[] { key }, new boolean[] { false });
	}

	@Override
	public String getLabel() {
		return "Health Bars";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if (!types.equals(EnumSet.of(TickType.CLIENT)) || !tickEnd) return;

		File cacheFile = IOUtils.getCacheFile(EnumVazkiiMods.HEALTH_BARS);
		NBTTagCompound comp = IOUtils.getTagCompoundInFile(cacheFile);
		ClientUtils.getClientPlayer();
		mod_HealthBars.barsEnabled = CommonUtils.flipBoolean(mod_HealthBars.barsEnabled);
		comp.setBoolean("barsEnabled", mod_HealthBars.barsEnabled);
		IOUtils.injectNBTToFile(comp, cacheFile);
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
