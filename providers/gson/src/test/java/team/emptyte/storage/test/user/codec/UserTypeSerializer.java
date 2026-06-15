/*
 * This file is part of storage, licensed under the MIT License
 *
 * Copyright (c) 2025 Emptyte Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.emptyte.storage.test.user.codec;

import com.google.gson.JsonObject;
import org.jspecify.annotations.NonNull;
import team.emptyte.storage.serialization.Reader;
import team.emptyte.storage.serialization.Writer;
import team.emptyte.storage.test.user.User;

public enum UserTypeSerializer implements team.emptyte.storage.serialization.TypeSerializer<User, JsonObject> {
  INSTANCE;

  @Override
  public void serialize(final User object, final Writer<JsonObject> writer) {
    writer
      .writeString("id", object.id())
      .writeString("name", object.name())
      .end();
  }

  @Override
  public @NonNull User deserialize(final Reader<JsonObject> reader) {
    final String id = reader.readString("id");
    if (id == null) {
      throw new IllegalArgumentException("id is null");
    }
    final String name = reader.readString("name");
    if (name == null) {
      throw new IllegalArgumentException("name is null");
    }
    return new User(id, name);
  }
}
