package me.retrodaredevil.solarthing.packet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;

import java.util.GregorianCalendar;

import me.retrodaredevil.solarthing.packet.fx.FXStatusPackets;
import me.retrodaredevil.solarthing.packet.mxfm.MXFMStatusPackets;
import me.retrodaredevil.util.json.JsonFile;

public final class StatusPackets {
	private StatusPackets(){ throw new UnsupportedOperationException(); }

	public static StatusPacket createFromJson(JsonObject jsonObject) throws IllegalArgumentException{
		final PacketType packetType = PacketType.valueOf(jsonObject.getAsJsonPrimitive("packetType").getAsString());
		switch(packetType){
			case FX_STATUS:
				return FXStatusPackets.createFromJson(jsonObject);
			case MXFM_STATUS:
				return MXFMStatusPackets.createFromJson(jsonObject);
			case FLEXNET_DC_STATUS:
				throw new UnsupportedOperationException("FLEXNet Status Packets aren't supported yet.");
			default:
				throw new UnsupportedOperationException();
		}
	}
	public static <T> T getOrNull(JsonObject jsonObject, String memberName, Getter<T, JsonElement> getter){
		JsonElement element = jsonObject.get(memberName);
		if(element == null){
			return null;
		}
		return getter.get(element);
	}

	/**
	 * Should not be instantiated using an anonymous class, should use a lambda instead.
	 * @param <T> The return value type
	 * @param <H> The input value type
	 */
	public interface Getter<T, H>{
		T get(H h);
	}
	public static void main(String[] args){
		CouchDbClient client = new CouchDbClient(new CouchDbProperties("solarthing", false, "http", "localhost", 5984, "admin", "relax"));
		for(JsonObject object : client.view("packets/millis").startKey(0).query(JsonObject.class)){
			JsonObject value = object.getAsJsonObject("value");
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(value.get("dateMillis").getAsLong());
			PacketCollection packetCollection = PacketCollections.createFromJson(value);
			System.out.println(JsonFile.gson.toJson(packetCollection));
		}
	}
}
