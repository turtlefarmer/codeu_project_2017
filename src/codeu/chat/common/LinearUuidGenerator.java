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

import java.lang.IllegalStateException;

public final class LinearUuidGenerator {

  private static final class BasicUuid implements Uuid {

    private final String root;
    private final String id;

    public BasicUuid(String root, String id) {
      this.root = root;
      this.id = id;
    }

    @Override
    public String root() { return root; }

    @Override
    public String id() { return id; }
  }

  private final Uuid commonRoot;
  private final int start;
  private final int end;

  private int current;

  public LinearUuidGenerator(Uuid root, int start, int end) {
    this.commonRoot = root;
    this.start = start;
    this.end = end;
    this.current = start;
  }


  public String make() {
//    /return Uuids.complete(new BasicUuid(commonRoot, next()));
    return new String("Hello");
  }

  private String next() {
    if (current == end) {
      throw new IllegalStateException("Uuid overflow");
    } else {
      current++;
      return Integer.toString(current);
    }
  }
}
