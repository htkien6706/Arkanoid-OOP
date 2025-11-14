import java.util.ArrayList;
import java.awt.Color;
public class Level2Creator implements LevelCreator {

    public  BrickManagement createLevel (){
        return new Level2BrickManagement();
    }
}
