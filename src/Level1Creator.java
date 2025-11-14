import java.awt.*;
import java.util.ArrayList;

public class Level1Creator implements LevelCreator {


    public  BrickManagement createLevel (){
       return  new Level1BrickManagement();

    }
}
