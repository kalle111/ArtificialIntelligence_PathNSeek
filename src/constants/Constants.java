package constants;

public class Constants {
    private Constants() {

    }

    public static final String TITLE="AI for Games - Assignment1";

    //application speed
    public int game_spd = 2;
    //positions
    public static final int agent_start_x = 100;
    public static final int agent_start_y = 100;

    public static final int tile_size = 32;
    public static final int first_tile_x = 20;
    public static final int first_tile_y = 20;

    public static final int width = 60*tile_size +first_tile_x*2;
    public static final int height = 800;

    //images for the game
    public static final String AGENT_URL = "src/image/pfeil2.png";
    public static final String OBSTACLE_URL = "src/image/wall.png";
    public static final String PATH_URL = "src/image/way.png";
    public static final String BACKGROUND_URL = "src/image/background.png";
    /*
    public static final String AGENT_URL3 = "src/image/pfeil2.png";
    public static final String AGENT_URL4 = "src/image/pfeil2.png";
    public static final String AGENT_URL5 = "src/image/pfeil2.png";
     */
}
