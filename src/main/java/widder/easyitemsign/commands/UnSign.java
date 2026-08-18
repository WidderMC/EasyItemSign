package widder.easyitemsign.commands;

import widder.easyitemsign.EasyItemSign;

import static widder.easyitemsign.load.signature;

public class UnSign {

    public static int unsign() {

        EasyItemSign.LOGGER.info(""+ signature);

        EasyItemSign.LOGGER.info("unsign");

        return 1;
    }

}
