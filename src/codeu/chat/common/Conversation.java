// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.chat.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;

import codeu.chat.util.Serializer;
import codeu.chat.util.Serializers;

public final class Conversation {

  public static final Serializer<Conversation> SERIALIZER = new Serializer<Conversation>() {

    @Override
    public void write(OutputStream out, Conversation value) throws IOException {

      Serializers.STRING.write(out, value.id.toString());
      Serializers.STRING.write(out, value.owner.toString());
      Time.SERIALIZER.write(out, value.creation);
      Serializers.STRING.write(out, value.title);
      Serializers.collection(Serializers.STRING).write(out, value.users);
      Serializers.STRING.write(out, value.firstMessage.toString());
      Serializers.STRING.write(out, value.lastMessage.toString());

    }

    @Override
    public Conversation read(InputStream in) throws IOException {

      final Conversation value = new Conversation(
          Serializers.STRING.read(in),
          Serializers.STRING.read(in),
          Time.SERIALIZER.read(in),
          Serializers.STRING.read(in)
      );

      value.users.addAll(Serializers.collection(Serializers.STRING).read(in));

      value.firstMessage = Serializers.STRING.read(in);
      value.lastMessage = Serializers.STRING.read(in);

      return value;

    }
  };

  public final ConversationSummary summary;

  public final String id;
  public final String owner;
  public final Time creation;
  public final String title;
  public final Collection<String> users = new HashSet<>();
  public String firstMessage;
  public String lastMessage;

  public Conversation(String id, String owner, Time creation, String title) {

    this.id = id;
    this.owner = owner;
    this.creation = creation;
    this.title = title;

    this.summary = new ConversationSummary(id, owner, creation, title);

  }
}
