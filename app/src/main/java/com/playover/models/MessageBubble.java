package com.playover.models;

public class MessageBubble {

        private String content;
        private boolean myMessage;

        public MessageBubble(String content, boolean myMessage) {
            this.content = content;
            this.myMessage = myMessage;
        }

        public String getContent() {
            return content;
        }

        public boolean myMessage() {
            return myMessage;
        }
}
