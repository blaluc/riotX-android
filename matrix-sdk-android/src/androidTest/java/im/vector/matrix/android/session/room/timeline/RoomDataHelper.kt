/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.matrix.android.session.room.timeline

import im.vector.matrix.android.api.session.events.model.Content
import im.vector.matrix.android.api.session.events.model.Event
import im.vector.matrix.android.api.session.events.model.EventType
import im.vector.matrix.android.api.session.events.model.toContent
import im.vector.matrix.android.api.session.room.model.Membership
import im.vector.matrix.android.api.session.room.model.RoomMemberSummary
import im.vector.matrix.android.api.session.room.model.message.MessageTextContent
import im.vector.matrix.android.api.session.room.model.message.MessageType
import kotlin.random.Random

object RoomDataHelper {

    private const val FAKE_TEST_SENDER = "@sender:test.org"
    private val EVENT_FACTORIES = hashMapOf(
            0 to { createFakeMessageEvent() },
            1 to { createFakeRoomMemberEvent() }
    )

    fun createFakeListOfEvents(size: Int = 10): List<Event> {
        return (0 until size).mapNotNull {
            val nextInt = Random.nextInt(EVENT_FACTORIES.size)
            EVENT_FACTORIES[nextInt]?.invoke()
        }
    }

    fun createFakeEvent(type: String,
                        content: Content? = null,
                        prevContent: Content? = null,
                        sender: String = FAKE_TEST_SENDER,
                        stateKey: String = FAKE_TEST_SENDER
    ): Event {
        return Event(
                type = type,
                eventId = Random.nextLong().toString(),
                content = content,
                prevContent = prevContent,
                senderId = sender,
                stateKey = stateKey
        )
    }

    fun createFakeMessageEvent(): Event {
        val message = MessageTextContent(MessageType.MSGTYPE_TEXT, "Fake message #${Random.nextLong()}").toContent()
        return createFakeEvent(EventType.MESSAGE, message)
    }

    fun createFakeRoomMemberEvent(): Event {
        val roomMember = RoomMemberSummary(Membership.JOIN, "Fake name #${Random.nextLong()}").toContent()
        return createFakeEvent(EventType.STATE_ROOM_MEMBER, roomMember)
    }
}
