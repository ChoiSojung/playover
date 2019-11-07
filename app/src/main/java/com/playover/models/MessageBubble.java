package com.playover.models;

public class MessageBubble {

        private String userName;
        private String timeStamp;
        private String content;
        private boolean myMessage;

        public MessageBubble(String userName, String timeStamp, String content, boolean myMessage) {
            this.userName = userName;
            this.timeStamp = timeStamp;
            this.content = content;
            this.myMessage = myMessage;
        }

        public String getUserName() {return userName;}

        public String getTimeStamp() {return timeStamp;}

        public String getContent() {
            return content;
        }

        public boolean myMessage() {
            return myMessage;
        }
}
