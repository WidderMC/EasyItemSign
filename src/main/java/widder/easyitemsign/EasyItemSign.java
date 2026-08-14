package widder.easyitemsign;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import widder.easyitemsign.commands.command;

public class EasyItemSign implements ModInitializer {
	public static final String MOD_ID = "easyitemsign";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		command.RegisterCommand();


	}

	/*				ToDo´s
			Place Data Save
			Unsign Save
			Load Config Data
			Crafting Save
	 */
}