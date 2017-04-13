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

package codeu.chat.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import codeu.chat.common.Conversation;
import codeu.chat.common.ConversationSummary;
import codeu.chat.common.LinearUuidGenerator;
import codeu.chat.common.Message;
import codeu.chat.common.Time;
import codeu.chat.common.User;
import codeu.chat.common.Uuid;
import codeu.chat.database.DatabaseAccess;
import codeu.chat.database.Model.UserModel;
import codeu.chat.util.store.Store;
import codeu.chat.util.store.StoreAccessor;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;

public final class Model {

  private static final Comparator<Uuid> UUID_COMPARE = new Comparator<Uuid>() {

    @Override
    public int compare(Uuid a, Uuid b) {

      if (a == b) { return 0; }

      if (a == null && b != null) { return -1; }

      if (a != null && b == null) { return 1; }

      final int order = Integer.compare(a.id(), b.id());
      return order == 0 ? compare(a.root(), b.root()) : order;
    }
  };

  private static final Comparator<Time> TIME_COMPARE = new Comparator<Time>() {
    @Override
    public int compare(Time a, Time b) {
      return a.compareTo(b);
    }
  };

  private static final Comparator<String> STRING_COMPARE = String.CASE_INSENSITIVE_ORDER;

  private final Store<Uuid, User> userById = new Store<>(UUID_COMPARE);
  private final Store<Time, User> userByTime = new Store<>(TIME_COMPARE);
  private final Store<String, User> userByText = new Store<>(STRING_COMPARE);

  private final Store<Uuid, Conversation> conversationById = new Store<>(UUID_COMPARE);
  private final Store<Time, Conversation> conversationByTime = new Store<>(TIME_COMPARE);
  private final Store<String, Conversation> conversationByText = new Store<>(STRING_COMPARE);

  private final Store<Uuid, Message> messageById = new Store<>(UUID_COMPARE);
  private final Store<Time, Message> messageByTime = new Store<>(TIME_COMPARE);
  private final Store<String, Message> messageByText = new Store<>(STRING_COMPARE);

  private final Uuid.Generator userGenerations = new LinearUuidGenerator(null, 1, Integer.MAX_VALUE);
  private Uuid currentUserGeneration = userGenerations.make();

  private final DatabaseAccess access = new DatabaseAccess();
  private DatabaseReference ref = access.initialize();

  // users
  public void loadUsers() {
    DatabaseReference usersRef = ref.child("users");
    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        System.out.println("Users Loaded: " + dataSnapshot.toString());
        // TODO: fix
        for (DataSnapshot child : dataSnapshot.getChildren()) {
          UserModel user = child.getValue(UserModel.class);
          System.out.println("Name: " + user.name + ", Creation: " + user.creation);
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });
  }

  public void add(User user) {
    currentUserGeneration = userGenerations.make();
    DatabaseReference usersRef = ref.child("users");

    userById.insert(user.id, user);
    userByTime.insert(user.creation, user);
    userByText.insert(user.name, user);

    DatabaseReference newUserRef = usersRef.push();
    String userId = newUserRef.getKey();
    System.out.println("New user id: " + userId);
    newUserRef.setValue(new UserModel(user.name, user.creation.toString()));
  }

  public StoreAccessor<Uuid, User> userById() {
    return userById;
  }

  public StoreAccessor<Time, User> userByTime() {
    return userByTime;
  }

  public StoreAccessor<String, User> userByText() {
    return userByText;
  }

  public Uuid userGeneration() {
    return currentUserGeneration;
  }



  public void add(Conversation conversation) {
    conversationById.insert(conversation.id, conversation);
    conversationByTime.insert(conversation.creation, conversation);
    conversationByText.insert(conversation.title, conversation);
  }

  public StoreAccessor<Uuid, Conversation> conversationById() {
    return conversationById;
  }

  public StoreAccessor<Time, Conversation> conversationByTime() {
    return conversationByTime;
  }

  public StoreAccessor<String, Conversation> conversationByText() {
    return conversationByText;
  }

  public void add(Message message) {
    messageById.insert(message.id, message);
    messageByTime.insert(message.creation, message);
    messageByText.insert(message.content, message);
  }

  public StoreAccessor<Uuid, Message> messageById() {
    return messageById;
  }

  public StoreAccessor<Time, Message> messageByTime() {
    return messageByTime;
  }

  public StoreAccessor<String, Message> messageByText() {
    return messageByText;
  }
}
