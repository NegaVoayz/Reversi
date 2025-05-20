package view.renderer;

public class RendererFactory {
    public static Renderer getRenderer(String type) {
        RendererImplConsole rendererImplConsole = new RendererImplConsole();
        return switch (type) {
            case "console" -> rendererImplConsole;
            case "gui" -> null;
            default -> null;
        };
    }
}
