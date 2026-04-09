package com.averyvi.bilbo.data.frop

import java.nio.ByteBuffer
import java.nio.ByteOrder

fun buildProfileChangeMessage(frequency: Int, positionInOctive: Int, octive: Int): ByteArray {
    val buffer = ByteBuffer.allocate(19).order(ByteOrder.LITTLE_ENDIAN)

    // --- HEADER ---
    buffer.put('F'.code.toByte()) // SoM
    buffer.put('D'.code.toByte()) // FMT (Data message)
    buffer.put(0x20.toByte())  // FMF (Long data format)
    buffer.put(4.toByte()) // NoF (4 bytes)
    buffer.putShort(5.toShort()) // LoD (5 bytes)
    buffer.putShort(0xB3.toShort()) // Chksm
    buffer.put('D'.code.toByte()) // EoH

    // --- DATA BLOCKS ---
    // setting
    buffer.put(1.toByte()) // BL (1 byte)
    buffer.put('P'.code.toByte()) // BD ('P' for Profile)

    // freq
    buffer.put(2.toByte()) // BL (2 bytes)
    buffer.putShort(frequency.toShort()) // BD (Little Endian, 2 bytes)

    // Block 3: Position in Octave
    buffer.put(1.toByte()) // BL (1 byte)
    buffer.put(positionInOctive.toByte()) // BD (1 byte)

    // Block 4: Octave
    buffer.put(1.toByte()) // BL (1 byte)
    buffer.put(octive.toByte()) // BD (1 byte, correctly handles negatives like -1 via 2's complement)

    // --- END OF MESSAGE ---
    buffer.put('\n'.code.toByte()) // Append newline

    return buffer.array()
}