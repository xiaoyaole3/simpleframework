package demo.pattern.eventmode;

public class SingleClickEventListener implements EventListener{
    @Override
    public void processEvent(Event event) {
        if ("sigleclick".equals(event.getType())) {
            System.out.println("Single click success.");
        }
    }
}
