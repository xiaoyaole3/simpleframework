package demo.pattern.eventmode;

public class DoubleClickEventListener implements EventListener{
    @Override
    public void processEvent(Event event) {
        if ("doubleclick".equals(event.getType())) {
            System.out.println("Double click success.");
        }
    }
}
