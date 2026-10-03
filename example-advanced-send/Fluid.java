public class Fluid {
    public static void main(String[] args) throws Exception {
        new MessageSender().run();
        // The demo exits after one pass; keep the container up long enough
        // for `docker compose logs` to capture it before compose tears down.
        Thread.sleep(10000);
    }
}
