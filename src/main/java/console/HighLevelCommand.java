package console;

import picocli.CommandLine;

@CommandLine.Command(name = "highLevelCommand",
        subcommands = {
                Start.class,
                Stop.class,
                Get.class,
                Remove.class,
                Cache.class,
                Exit.class
        })
public class HighLevelCommand implements Runnable {

    @Override
    public void run(){
    }
}
