/*
 * Copyright 2017 Christian Basler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.dissem.msgpack.types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Representation of a msgpack encoded boolean.
 */
public class MPBoolean implements MPType<Boolean> {
    private final static int FALSE = 0xC2;
    private final static int TRUE = 0xC3;

    private final boolean value;

    public MPBoolean(boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    public void pack(OutputStream out) throws IOException {
        if (value) {
            out.write(TRUE);
        } else {
            out.write(FALSE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MPBoolean mpBoolean = (MPBoolean) o;
        return value == mpBoolean.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public String toJson() {
        return String.valueOf(value);
    }

    public static class Unpacker implements MPType.Unpacker<MPBoolean> {

        public boolean is(int firstByte) {
            return firstByte == TRUE || firstByte == FALSE;
        }

        public MPBoolean unpack(int firstByte, InputStream in) {
            if (firstByte == TRUE) {
                return new MPBoolean(true);
            } else if (firstByte == FALSE) {
                return new MPBoolean(false);
            } else {
                throw new IllegalArgumentException(String.format("0xC2 or 0xC3 expected but was 0x%02x", firstByte));
            }
        }
    }
}
