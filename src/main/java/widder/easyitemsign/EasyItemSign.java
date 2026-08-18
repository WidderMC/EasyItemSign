package widder.easyitemsign;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import widder.easyitemsign.commands.command;

public class EasyItemSign implements ModInitializer {
	public static final String MOD_ID = "EasyItemSign";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		EasyItemSign.LOGGER.info("Loading EasyItemSign");

		load.load();
		command.RegisterCommand();
	}

	/*				ToDo´s
			Unsign Save

			Bookmarks (schift + f11)


			Crafting Save
			Place Data Save
	 */
}