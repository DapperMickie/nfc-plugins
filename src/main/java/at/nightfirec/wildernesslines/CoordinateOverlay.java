package at.nightfirec.wildernesslines;

import java.awt.Dimension;
import java.awt.Graphics2D;
import static java.util.Arrays.asList;
import java.util.List;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Varbits;
import net.runelite.api.WorldView;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

public class CoordinateOverlay extends OverlayPanel
{
	private final Client client;
	private final WildernessLinesConfig config;

	@Inject
	public CoordinateOverlay(Client client, WildernessLinesPlugin plugin, WildernessLinesConfig config)
	{
		super(plugin);
		this.client = client;
		this.config = config;
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT); // Position can be adjusted
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.developerMode() || !config.currentPlayer())
		{
			return null;
		}
		List<LayoutableRenderableEntity> children = panelComponent.getChildren();

		Player localPlayer = client.getLocalPlayer();
		WorldView worldView = localPlayer.getWorldView();
		WorldPoint worldPoint = localPlayer.getWorldLocation();

		children.addAll(asList(
			TitleComponent.builder()
				.text("Wilderness Lines Devtool")
				.build(),
			LineComponent.builder()
				.left("My Player X:")
				.right(String.valueOf(worldPoint.getX()))
				.build(),
			LineComponent.builder()
				.left("My Player Y:")
				.right(String.valueOf(worldPoint.getY()))
				.build(),
			LineComponent.builder()
				.left("WorldView:")
				.right(String.valueOf(worldView.getId()))
				.build(),
			LineComponent.builder()
				.left("Plane:")
				.right(String.valueOf(worldView.getPlane()))
				.build(),
			LineComponent.builder()
				.left("In Multi (1t delay):")
				.right(client.getVarbitValue(Varbits.MULTICOMBAT_AREA) == 0 ? "No" : "Yes")
				.build()
			)
		);

		panelComponent.setPreferredSize(new Dimension(graphics.getFontMetrics().stringWidth("Wilderness      Lines     Devtool"), 0));

		return super.render(graphics);
	}
}

