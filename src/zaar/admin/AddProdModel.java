package zaar.admin;

import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;

import java.io.File;

public class AddProdModel{

    public void getTopHBox(HBox hBox){
        ToolsSingleton tS = ToolsSingleton.getInstance();
        ScreenSingleton sS = ScreenSingleton.getInstance();
        tS.setButtonTopHBox(hBox, "View products", sS.new OpenProductScreen());
        tS.setButtonTopHBox(hBox, "Admin tools", sS.new OpenManageDatabase());
    }
}
