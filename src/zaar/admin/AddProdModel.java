package zaar.admin;

import javafx.scene.layout.HBox;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;

public class AddProdModel{

    public void getTopHBox(HBox hBox){
        ToolsSingleton tS = ToolsSingleton.getInstance();
        ScreenSingleton sS = ScreenSingleton.getInstance();
        tS.setButtonTopHBox(hBox, "View products", sS.new OpenProductScreen());
        tS.setButtonTopHBox(hBox, "Admin tools", sS.new OpenManageDatabase());
    }
}
