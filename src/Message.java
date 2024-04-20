public class Message {
    public Command messageCmd;
    public Object messageContent;

    public Message(Command messageCmd, Object messageContent) {
        this.messageCmd = messageCmd;
        this.messageContent = messageContent;
    }
}