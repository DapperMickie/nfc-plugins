package at.nightfirec.wildernesslines;

import at.nightfirec.wildernesslines.panel.PanelOriginFilter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.IconTextField;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.ImageUtil;

@Singleton
class MultiLinePluginPanel extends PluginPanel
{
	private final Client client;
	private final WildernessLinesPlugin plugin;
	private final WildernessLinesConfig config;

	private static final ImageIcon ADD_ICON;
	private static final ImageIcon ADD_HOVER_ICON;
	private final JLabel markerAdd = new JLabel(ADD_ICON);

	private static final ImageIcon[] FILTER_ICONS;
	private final JLabel filter = new JLabel(FILTER_ICONS[0]);
	private final IconTextField searchBar = new IconTextField();
	private final JPanel searchPanel = new JPanel(new BorderLayout());

	private final PluginErrorPanel noAreaGroupsPanel = new PluginErrorPanel();
	private final JPanel areaGroupsView = new JPanel();

	private static final String[] FILTER_DESCRIPTIONS =
		{
			"<html>Filter:<br>Listing all area groups</html>",
			"<html>Filter:<br>Listing only plugin area groups</html>",
			"<html>Filter:<br>Listing only custom area groups</html>"
		};

	private static final String[] FILTER_TEXT = {"ALL", "P", "C"};

	@Getter
	private PanelOriginFilter panelFilter = PanelOriginFilter.ALL;

	static
	{
		final BufferedImage addIcon = ImageUtil.loadImageResource(WildernessLinesPlugin.class, "add_icon.png");
		ADD_ICON = new ImageIcon(addIcon);
		ADD_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(addIcon, 0.53f));

		// TODO: change for actual icons
		FILTER_ICONS = new ImageIcon[]
			{
				new ImageIcon(addIcon),
				new ImageIcon(addIcon),
				new ImageIcon(addIcon)
			};
	}

	@Inject
	public MultiLinePluginPanel(Client client, WildernessLinesPlugin plugin, WildernessLinesConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;

		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel northPanel = createNorthPanel();

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

		areaGroupsView.setLayout(new BoxLayout(areaGroupsView, BoxLayout.Y_AXIS));
		areaGroupsView.setBackground(ColorScheme.DARK_GRAY_COLOR);

		noAreaGroupsPanel.setVisible(false);

		areaGroupsView.add(noAreaGroupsPanel);
		centerPanel.add(areaGroupsView, BorderLayout.NORTH);

		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
	}

	private JPanel createNorthPanel()
	{
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.setBorder(new EmptyBorder(1, 0, 10, 0));

		JPanel titlePanel = createTitlePanel();
		northPanel.add(titlePanel, BorderLayout.NORTH);

		createSearchPanel();
		northPanel.add(searchPanel, BorderLayout.CENTER);

		return northPanel;
	}

	private JPanel createTitlePanel()
	{
		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBorder(new EmptyBorder(1, 3, 10, 7));
		titlePanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

		JLabel title = new JLabel();
		title.setText("Multi Lines");
		title.setForeground(Color.WHITE);

		JPanel buttons = createTitleButtons();

		titlePanel.add(title, BorderLayout.WEST);
		titlePanel.add(buttons, BorderLayout.EAST);

		return titlePanel;
	}

	private JPanel createTitleButtons()
	{
		JPanel markerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 3));

		setNewAreaButtonValues();
		markerButtons.add(markerAdd);

		return markerButtons;
	}

	private void setNewAreaButtonValues()
	{
		markerAdd.setToolTipText("Add new area");
		markerAdd.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				//addNewArea();
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				markerAdd.setIcon(ADD_HOVER_ICON);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				markerAdd.setIcon(ADD_ICON);
			}
		});
	}

	private void createSearchPanel()
	{
		//JPanel searchPanel = new JPanel();
		searchPanel.setBorder(new EmptyBorder(1, 0, 0, 0));

		//IconTextField searchBar = new IconTextField();
		searchBar.setIcon(IconTextField.Icon.SEARCH);
		searchBar.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		searchBar.setHoverBackgroundColor(ColorScheme.DARKER_GRAY_HOVER_COLOR);
		searchBar.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH - 43 - filter.getWidth(), 24));
		searchBar.addActionListener(e -> rebuild());
		searchBar.addClearListener(this::rebuild);
		searchBar.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					searchBar.setText("");
					rebuild();
				}
			}
		});

		filter.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		filter.setOpaque(true);
		filter.setPreferredSize(new Dimension(28, 24));
		filter.setText(FILTER_TEXT[0]);
		filter.setToolTipText(FILTER_DESCRIPTIONS[0]);
		filter.setHorizontalTextPosition(JLabel.CENTER);
		filter.setFont(FontManager.getRunescapeSmallFont());
		filter.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent mouseEvent)
			{
				panelFilter = panelFilter.next();
				filter.setText(FILTER_TEXT[panelFilter.ordinal()]);
				filter.setIcon(FILTER_ICONS[panelFilter.ordinal()]);
				filter.setToolTipText(FILTER_DESCRIPTIONS[panelFilter.ordinal()]);
				rebuild();
			}

			@Override
			public void mouseEntered(MouseEvent mouseEvent)
			{
				filter.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
			}

			@Override
			public void mouseExited(MouseEvent mouseEvent)
			{
				filter.setBackground(ColorScheme.DARKER_GRAY_COLOR);
			}
		});

		searchPanel.add(searchBar, BorderLayout.WEST);
		searchPanel.add(filter, BorderLayout.EAST);
	}

	public void rebuild()
	{
		areaGroupsView.removeAll();

		List<AreaGroup> areaGroups = plugin.getLoadedAreaGroups();

		for (final AreaGroup areaGroup : areaGroups)
		{
			if (panelFilter == PanelOriginFilter.ALL ||
				(areaGroup.getOrigin() == AreaOrigin.PLUGIN && panelFilter == PanelOriginFilter.PLUGIN) ||
				(areaGroup.getOrigin() == AreaOrigin.CUSTOM && panelFilter == PanelOriginFilter.CUSTOM))
			{
				areaGroupsView.add(new MultiLinePanel(plugin, config, areaGroup));
				areaGroupsView.add(Box.createRigidArea(new Dimension(0, 10)));
			}
		}

		boolean empty = areaGroupsView.getComponentCount() == 0;
		noAreaGroupsPanel.setContent("Multi Lines",
			"Click the '+' button to add a new multi line");
		noAreaGroupsPanel.setVisible(empty);
		searchPanel.setVisible(!empty);

		if (empty && areaGroups.size() > 0)
		{
			noAreaGroupsPanel.setContent("Multi Lines",
				"No area groups are available for the current search term and/or selected filter.");
			searchPanel.setVisible(true);
		}

		areaGroupsView.add(noAreaGroupsPanel);

		repaint();
		revalidate();
	}
}
