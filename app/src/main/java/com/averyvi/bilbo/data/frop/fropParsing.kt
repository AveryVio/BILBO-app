package com.averyvi.bilbo.data.frop

import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder

class FropParser {
    private val buffer = mutableListOf<Byte>()
    private val EOL: Byte = '\n'.code.toByte()

    fun processIncomingBytes(data: ByteArray): List<FropMessage> {
        buffer.addAll(data.toList())
        val parsedMessages = mutableListOf<FropMessage>()

        while (true) {
            val eolIndex = buffer.indexOf(EOL) // index of first newline
            if (eolIndex == -1) break

            val packet = buffer.subList(0, eolIndex).toByteArray() // offset is intentional, newline is not needed past this point

            buffer.subList(0, eolIndex + 1).clear()

            parsePacket(packet)?.let { parsedMessages.add(it) }
        }
        return parsedMessages
    }

    private fun parsePacket(packet: ByteArray): FropMessage? {
        if (packet.isEmpty() || packet[0] != 'F'.code.toByte()) return null // Not a valid FROP message

        // ByteBuffer is a replacement for a Union (I get it now and I'm hooked)
        val byteBuffer = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN)

        try {
            val startOfMessage = byteBuffer.get().toInt().toChar()
            val messageType = byteBuffer.get().toInt().toChar()
            val messageFormat = byteBuffer.get().toInt()
            val numberOfFields = byteBuffer.get().toInt()
            val endOfHeader = byteBuffer.get().toInt().toChar()

            // Check message type
            if (messageType == 'D' && messageFormat == 0x10 && numberOfFields == 5) {

                // setting
                byteBuffer.get()
                val setting = byteBuffer.get().toInt().toChar()

                if (setting == 'T') {
                    // octive
                    byteBuffer.get()
                    val oct = byteBuffer.get().toInt() and 0xFF // `and 0xFF` makes it unsigned

                    // note
                    byteBuffer.get()
                    val pos = byteBuffer.get().toInt() and 0xFF

                    // freq
                    byteBuffer.get()
                    // getShort() reads 2 bytes in Little Endian automatically
                    val freq = byteBuffer.short.toInt() and 0xFFFF

                    // pitch diff
                    byteBuffer.get()
                    val diff = byteBuffer.get().toInt() and 0xFF

                    return TuningData(oct, pos, freq, diff)
                }
            }
        } catch (e: Exception) {
            Log.e("FropParser", "Buffer underflow or malformed packet", e)
        }

        return null
    }
}