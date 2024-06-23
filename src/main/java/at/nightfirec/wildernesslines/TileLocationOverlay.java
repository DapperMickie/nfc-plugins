package at.nightfirec.wildernesslines;

import static java.awt.Color.GREEN;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

@Singleton
public class TileLocationOverlay extends Overlay
{
	private static final Font FONT = FontManager.getRunescapeFont().deriveFont(Font.BOLD, 16);

	private final Client client;
	private final WildernessLinesConfig config;
	private final TooltipManager toolTipManager;

	@Inject
	private TileLocationOverlay(Client client, WildernessLinesConfig config, TooltipManager toolTipManager)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(PRIORITY_HIGHEST);
		this.client = client;
		this.config = config;
		this.toolTipManager = toolTipManager;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.developerMode() || !config.tileLocations()) {
			return null;
		}
		graphics.setFont(FONT);
		renderTileObjects(graphics);
		return null;
	}

	private void renderTileObjects(Graphics2D graphics)
	{
		Player player = client.getLocalPlayer();
		WorldView wv = player.getWorldView();
		Scene scene = wv.getScene();
		Tile[][][] tiles = scene.getTiles();

		int z = wv.getPlane();

		for (int x = 0; x < Constants.SCENE_SIZE; ++x)
		{
			for (int y = 0; y < Constants.SCENE_SIZE; ++y)
			{
				Tile tile = tiles[z][x][y];

				if (tile == null)
				{
					continue;
				}

				renderTileTooltip(graphics, tile);
			}
		}
	}

	private void renderTileTooltip(Graphics2D graphics, Tile tile)
	{
		final LocalPoint tileLocalLocation = tile.getLocalLocation();
		Polygon poly = Perspective.getCanvasTilePoly(client, tileLocalLocation);
		if (poly != null && poly.contains(client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY()))
		{
			WorldPoint worldLocation = WorldPoint.fromLocalInstance(client, tileLocalLocation);
			String tooltip = String.format("World location: %d, %d, %d",
				worldLocation.getX(), worldLocation.getY(), worldLocation.getPlane());
			toolTipManager.add(new Tooltip(tooltip));
			OverlayUtil.renderPolygon(graphics, poly, GREEN);
		}
	}
}
