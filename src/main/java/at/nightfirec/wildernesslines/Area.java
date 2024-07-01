package at.nightfirec.wildernesslines;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import lombok.AllArgsConstructor;
import net.runelite.api.Client;
import net.runelite.api.WorldView;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.worldmap.WorldMapPoint;

@AllArgsConstructor
public class Area
{
	private final int southWestX;
	private final int southWestY;
	private final int northEastX;
	private final int northEastY;
	private final int plane;
	private final int worldViewId;

	public WorldPoint getSWWorldPoint(Client client)
	{
		WorldView wv = client.getWorldView(worldViewId);
		return WorldPoint.fromScene(wv, southWestX, southWestY, plane);
	}

	public WorldPoint getNEWorldPoint(Client client)
	{
		WorldView wv = client.getWorldView(worldViewId);
		return WorldPoint.fromScene(wv, southWestX, southWestY, plane);
	}

	public WorldArea getWorldArea()
	{
		int height = northEastY - southWestY + 1;
		int width = northEastX - southWestX + 1;
		return new WorldArea(southWestX, southWestY, width, height, plane);
	}

	public Rectangle getRectangle()
	{
		int height = northEastY - southWestY + 1;
		int width = northEastX - southWestX + 1;
		return new Rectangle(southWestX, southWestY, width, height);
	}

	public Line2D.Float getLine2DFloat()
	{
		return new Line2D.Float(southWestX, southWestY, northEastX, northEastY);
	}

	public WorldMapPoint getSWWorldMapPoint(Client client)
	{
		return WorldMapPoint.builder().worldPoint(getSWWorldPoint(client)).build();
	}

	public WorldMapPoint getNEWorldMapPoint(Client client)
	{
		return WorldMapPoint.builder().worldPoint(getNEWorldPoint(client)).build();
	}
}
