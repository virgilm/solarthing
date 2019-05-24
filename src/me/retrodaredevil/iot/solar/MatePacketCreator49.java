package me.retrodaredevil.iot.solar;

import me.retrodaredevil.iot.packets.Packet;
import me.retrodaredevil.iot.packets.StartEndPacketCreator;
import me.retrodaredevil.iot.solar.fx.FXStatusPackets;
import me.retrodaredevil.iot.solar.mx.MXStatusPackets;
import me.retrodaredevil.iot.util.CheckSumException;
import me.retrodaredevil.iot.util.IgnoreCheckSum;
import me.retrodaredevil.iot.util.ParsePacketAsciiDecimalDigitException;
import me.retrodaredevil.util.json.JsonFile;

import java.util.Collection;
import java.util.Collections;

public class MatePacketCreator49 extends StartEndPacketCreator {

	private final IgnoreCheckSum ignoreCheckSum;
	private final char[] bytes = new char[49];
	/** The amount of char elements initialized in the bytes array */

	public MatePacketCreator49(IgnoreCheckSum ignoreCheckSum){
		super('\n', '\r', 49, 49);
		this.ignoreCheckSum = ignoreCheckSum;
	}
	public MatePacketCreator49(){
		this(IgnoreCheckSum.DISABLED);
	}

	@Override
	public Collection<Packet> create(char[] bytes) throws PacketCreationException{
		final int value = (int) bytes[1]; // ascii value
		final Packet r;
		if(value >= 48 && value <= 58){ // fx status
			try {
				r = FXStatusPackets.createFromChars(bytes, ignoreCheckSum);
			} catch (ParsePacketAsciiDecimalDigitException | CheckSumException e) {
				throw new PacketCreationException(e);
			}
		} else if(value >= 65 && value <= 75){ // mx
			try {
				r = MXStatusPackets.createFromChars(bytes, ignoreCheckSum);
			} catch (ParsePacketAsciiDecimalDigitException | CheckSumException e) {
				throw new PacketCreationException(e);
			}
		} else if(value >= 97 && value <= 106){
			throw new UnsupportedOperationException("Not set up to use FLEXnet DC Status Packets. value: " + value);
		} else {
			throw new UnsupportedOperationException("Ascii value: " + value + " not supported. (from: '" + new String(bytes) + "')");
		}
		System.out.println("=====");
		System.out.println(JsonFile.gson.toJson(r));
		System.out.println("=====");
		return Collections.singleton(r);
	}

}
