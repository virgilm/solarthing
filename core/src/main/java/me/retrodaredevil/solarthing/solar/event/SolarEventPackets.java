package me.retrodaredevil.solarthing.solar.event;

import com.google.gson.JsonObject;
import me.retrodaredevil.solarthing.packets.UnknownPacketTypeException;
import me.retrodaredevil.solarthing.solar.outback.fx.event.FXDayEndPackets;

public final class SolarEventPackets {
	private SolarEventPackets(){ throw new UnsupportedOperationException(); }
	/**
	 * @param jsonObject The {@link JsonObject} to create the {@link SolarEventPacket} from
	 * @return The {@link SolarEventPacket} created from {@code jsonObject}
	 * @throws UnknownPacketTypeException thrown if {@code jsonObject} isn't a {@link SolarEventPacket}
	 */
	public static SolarEventPacket createFromJson(JsonObject jsonObject) {
		final String packetName = jsonObject.getAsJsonPrimitive("packetType").getAsString();
		final SolarEventPacketType packetType;
		try {
			packetType = SolarEventPacketType.valueOf(packetName);
		} catch (IllegalArgumentException e) {
			throw new UnknownPacketTypeException("packet type name: " + packetName, e);
		}
		switch(packetType){
			case FX_DAILY_DAY_END:
				return FXDayEndPackets.createFromJson(jsonObject);
			default:
				throw new UnsupportedOperationException("" + packetType);
		}
	}
}
