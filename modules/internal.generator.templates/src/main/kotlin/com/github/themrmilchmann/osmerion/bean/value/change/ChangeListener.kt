/*
 * Copyright (c) 2017 Leon Linhart,
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.themrmilchmann.osmerion.bean.value.change

import com.github.themrmilchmann.osmerion.bean.value.*
import com.github.themrmilchmann.osmerion.internal.generator.*
import com.github.themrmilchmann.osmerion.internal.generator.java.*
import com.github.themrmilchmann.osmerion.internal.generator.java.Type
import java.lang.reflect.*

private fun name(type: PrimitiveType) = "${type.abbrevName}ChangeListener"
fun ChangeListener(type: PrimitiveType) = if (types.contains(type)) Type(name(type), packageName) else throw IllegalArgumentException("")

val ChangeListener = Profile {
    types.forEach {
        val t_value = it

        javaInterface(name(t_value), packageName, MODULE_BASE, visibility = Modifier.PUBLIC) {
            addAnnotations(FunctionalInterface)

            documentation = "A specialized {@code $t_value} {@link ChangeListener}."
            authors(AUTHOR_LEON_LINHART)
            since = VERSION_1_0_0_0

            void.method(
                "onChanged",
                "Processes a value change of an ObservableValue this listener is attached to.",

                ObservableValue(t_value).PARAM("observable", "the observable whose value has changed"),
                t_value.PARAM("oldValue", "the old value"),
                t_value.PARAM("newValue", "the new value"),

                since = VERSION_1_0_0_0
            )

            this.method(
                "wrap",
                """
                Returns a specialized ChangeListener wrapping around the given one. However, if the given {@code listener} already is a specialized listener of
                the same type, it is simply returned.

                The wrapper's hashcode is the same as the hashcode of the wrapped listener. The wrapper's {@code equals()} method only returns true if the
                wrapped listener is passed as argument.
                """,

                ParametrizedType("ChangeListener", packageName, "? super ${t_value.boxedType}").PARAM("listener", "the listener to be wrapped"),

                visibility = Modifier.STATIC,
                returnDoc = "a specialized ChangeListener wrapping around the given one",
                since = VERSION_1_0_0_0,

                body = """
return (listener instanceof ${name(t_value)}) ? (${name(t_value)}) listener : new ${name(t_value)}() {

    @Override
    public void onChanged(${ObservableValue(t_value)} observable, $t_value oldValue, $t_value newValue) {
        listener.onChanged(observable, oldValue, newValue);
    }

    @Override
    public boolean equals(Object other) {
        return other == listener || other == this;
    }

    @Override
    public int hashCode() {
        return listener.hashCode();
    }

};
"""
            )
        }
    }
}