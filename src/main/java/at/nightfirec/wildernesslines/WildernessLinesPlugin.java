/*
 * Copyright (c) 2018, Woox <https://github.com/wooxsolo>
 * Copyright (c) 2021, Jordan <nightfirecat@protonmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package at.nightfirec.wildernesslines;

import com.google.inject.Provides;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.ItemID;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.geometry.Geometry;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Wilderness Lines",
	description = "Show wilderness multicombat areas, the dragon spear range to those areas, and level 20 and 30 lines.",
	tags = {"dragon spear", "multicombat", "wildy", "20", "30", "wilderness level"}
)
public class WildernessLinesPlugin extends Plugin
{
	private static final int SPEAR_RANGE = 4;

	private static final Area MULTI_AREA = new Area();
	private static final Area SPEAR_MULTI_AREA = new Area();

	static
	{
		AreaGroup wildernessAreaGroup = PluginAreaGroups.getWildernessAreaGroup();
		for (final at.nightfirec.wildernesslines.Area multiArea : wildernessAreaGroup.getAreas())
		{
			MULTI_AREA.add(new Area(multiArea.getRectangle()));
			for (int i = 0; i <= SPEAR_RANGE; i++)
			{
				final Rectangle spearArea = new Rectangle(multiArea.getRectangle());
				spearArea.grow(SPEAR_RANGE - i, i);
				SPEAR_MULTI_AREA.add(new Area(spearArea));
			}
		}
	}

	@Inject
	private WildernessLinesOverlay overlay;

	@Inject
	private CoordinateOverlay coordinateOverlay;

	@Inject
	private CoordinateSidePanel coordinateSidePanel;

	@Inject
	private MultiLinePluginPanel multiLinePluginPanel;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private Client client;

	@Inject
	private WildernessLinesConfig config;

	@Inject
	private ItemManager itemManager;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private TileLocationOverlay tileLocationOverlay;

	private CoordinateSidePanel panel;
	private NavigationButton navButton;
	private NavigationButton navButton2;

	private Rectangle currentRectangle = null;
	private Area currentArea = null;


	@Provides
	WildernessLinesConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WildernessLinesConfig.class);
	}

	@Override
	public void startUp()
	{
		itemManager.getImage(ItemID.DRAGON_SPEAR);
		overlayManager.add(overlay);
		overlayManager.add(coordinateOverlay);
		overlayManager.add(tileLocationOverlay);

		panel = new CoordinateSidePanel(client, config, this);

		navButton =
			NavigationButton.builder()
				.tooltip("Wilderness Lines")
				.icon(itemManager.getImage(ItemID.DRAGON_SPEAR))
				.panel(panel)
				.build();
		navButton2 =
			NavigationButton.builder()
				.tooltip("Multi Lines")
				.icon(itemManager.getImage(ItemID.DRAGON_SPEAR))
				.panel(multiLinePluginPanel)
				.build();
		if (config.developerMode())
		{
			clientToolbar.addNavigation(navButton);
			clientToolbar.addNavigation(navButton2);
		}

		loadAreaGroups();
		multiLinePluginPanel.rebuild();
	}

	@Override
	public void shutDown()
	{
		overlayManager.remove(overlay);
		overlayManager.remove(coordinateOverlay);
		overlayManager.remove(tileLocationOverlay);
		clientToolbar.removeNavigation(navButton);
		clientToolbar.removeNavigation(navButton2);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		// Check if the change is related to your plugin
		if (!event.getGroup().equals(WildernessLinesConfig.GROUP))
		{
			return;
		}

		if (event.getKey().equals("developerMode"))
		{
			if (event.getNewValue().equals(String.valueOf(true)))
			{
				clientToolbar.addNavigation(navButton);
				clientToolbar.addNavigation(navButton2);
			}
			else
			{
				clientToolbar.removeNavigation(navButton);
				clientToolbar.removeNavigation(navButton2);
			}
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (config.developerMode())
		{
			panel.update();
		}
	}

	private Set<Area> spearAreas = new HashSet<>();

	private void removeSpearLines()
	{
		for (Area area : spearAreas)
		{
			SPEAR_MULTI_AREA.subtract(area);
		}
	}

	private void addSpearLines()
	{
		for (int i = 0; i <= SPEAR_RANGE; i++)
		{
			final Rectangle spearArea = new Rectangle(currentRectangle);
			spearArea.grow(SPEAR_RANGE - i, i);
			Area area = new Area(spearArea);
			SPEAR_MULTI_AREA.add(area);
			spearAreas.add(area);
		}
	}

	public void addLocation(Rectangle r)
	{
		if (currentRectangle != null)
		{
			MULTI_AREA.subtract(currentArea);
			removeSpearLines();
		}
		currentRectangle = r;
		currentArea = new Area(currentRectangle);
		MULTI_AREA.add(currentArea);
		addSpearLines();
	}

	private void transformWorldToLocal(float[] coords)
	{
		final Player player = client.getLocalPlayer();
		final LocalPoint lp = LocalPoint.fromWorld(player.getWorldView(), (int) coords[0], (int) coords[1]);
		coords[0] = lp.getX() - Perspective.LOCAL_TILE_SIZE / 2f;
		coords[1] = lp.getY() - Perspective.LOCAL_TILE_SIZE / 2f;
	}

	GeneralPath getMultiLinesToDisplay()
	{
		return getLinesToDisplay(MULTI_AREA);
	}

	GeneralPath getSpearLinesToDisplay()
	{
		return getLinesToDisplay(SPEAR_MULTI_AREA);
	}

	private List<AreaGroup> loadedAreaGroups = new ArrayList<>();

	public List<AreaGroup> getLoadedAreaGroups()
	{
		return loadedAreaGroups;
	}

	private void loadAreaGroups()
	{
		loadedAreaGroups.clear();
		//TODO: load saved area groups
		loadedAreaGroups.add(PluginAreaGroups.getWilderness20Lines());
		loadedAreaGroups.add(PluginAreaGroups.getWilderness30Lines());
		loadedAreaGroups.add(PluginAreaGroups.getWildernessAreaGroup());

		prepareAreaGroups();
	}

	private List<Shape> wilderness20LinesList = new ArrayList<>();
	private GeneralPath wilderness20LinesPath = null;
	private List<Shape> wilderness30LinesList = new ArrayList<>();
	private GeneralPath wilderness30LinesPath = null;

	private void prepareAreaGroups()
	{
		for (AreaGroup group : loadedAreaGroups)
		{
			for (at.nightfirec.wildernesslines.Area area : group.getAreas())
			{
				switch (group.getAreaType())
				{
					case WILDERNESS_20_LINE:
						wilderness20LinesList.add(area.getLine2DFloat());
						break;
					case WILDERNESS_30_LINE:
						wilderness30LinesList.add(area.getLine2DFloat());
						break;
					default:
						break;
				}
			}
		}
	}

	public GeneralPath getWilderness20LinesPath()
	{
		return getLinesToDisplay(wilderness20LinesList);
	}

	public GeneralPath getWilderness30LinesPath()
	{
		return getLinesToDisplay(wilderness30LinesList);
	}

	public GeneralPath getLinesToDisplay(final List<Shape> shapes)
	{
		final Player localPlayer = client.getLocalPlayer();
		final WorldView wv = localPlayer == null ? client.getTopLevelWorldView() : localPlayer.getWorldView();
		final Rectangle sceneRect = new Rectangle(
			wv.getBaseX() + 1, wv.getBaseY() + 1,
			Constants.SCENE_SIZE - 2, Constants.SCENE_SIZE - 2);

		final GeneralPath paths = new GeneralPath();
		for (final Shape shape : shapes)
		{
			GeneralPath lines = new GeneralPath(shape);
			lines = Geometry.clipPath(lines, sceneRect);
			lines = Geometry.splitIntoSegments(lines, 1);
			lines = Geometry.transformPath(lines, this::transformWorldToLocal);
			paths.append(lines, false);
		}
		return paths;
	}

	public GeneralPath getLinesToDisplay(final Shape... shapes)
	{
		final WorldView wv = client.getLocalPlayer().getWorldView();
		final Rectangle sceneRect = new Rectangle(
			wv.getBaseX() + 1, wv.getBaseY() + 1,
			Constants.SCENE_SIZE - 2, Constants.SCENE_SIZE - 2);

		final GeneralPath paths = new GeneralPath();
		for (final Shape shape : shapes)
		{
			GeneralPath lines = new GeneralPath(shape);
			lines = Geometry.clipPath(lines, sceneRect);
			lines = Geometry.splitIntoSegments(lines, 1);
			lines = Geometry.transformPath(lines, this::transformWorldToLocal);
			paths.append(lines, false);
		}
		return paths;
	}
}
