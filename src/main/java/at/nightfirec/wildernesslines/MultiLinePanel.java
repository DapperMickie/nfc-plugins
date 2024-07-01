package at.nightfirec.wildernesslines;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.components.FlatTextField;
import net.runelite.client.util.ImageUtil;

public class MultiLinePanel extends JPanel
{
	private static final Border NAME_BOTTOM_BORDER = new CompoundBorder(
		BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.DARK_GRAY_COLOR),
		BorderFactory.createLineBorder(ColorScheme.DARKER_GRAY_COLOR));

	private final FlatTextField nameInput = new FlatTextField();

	public MultiLinePanel(WildernessLinesPlugin plugin, WildernessLinesConfig config, AreaGroup areaGroup)
	{
		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARKER_GRAY_COLOR);
		setBorder(new EmptyBorder(0, 0, 0, 0));

		JPanel nameWrapper = new JPanel(new BorderLayout());
		nameWrapper.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		nameWrapper.setBorder(NAME_BOTTOM_BORDER);

		//add(new JLabel(areaGroup.getName()));

		JPanel nameActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 3));
		nameActions.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		nameInput.setText(areaGroup.getName());
		nameInput.setBorder(null);
		nameInput.setEditable(false);
		nameInput.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		nameInput.setPreferredSize(new Dimension(0, 24));
		nameInput.getTextField().setForeground(Color.WHITE);
		nameInput.getTextField().setBorder(new EmptyBorder(0, 5, 0, 0));

		nameWrapper.add(nameInput, BorderLayout.CENTER);
		JPanel markerContainer = new JPanel();
		markerContainer.setLayout(new BoxLayout(markerContainer, BoxLayout.Y_AXIS));
		markerContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);

		markerContainer.add(nameWrapper);
		add(markerContainer);
	}




	/*
	private static final Border NAME_BOTTOM_BORDER = new CompoundBorder(
		BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.DARK_GRAY_COLOR),
		BorderFactory.createLineBorder(ColorScheme.DARKER_GRAY_COLOR));

	private static final ImageIcon BORDER_COLOR_ICON;
	private static final ImageIcon BORDER_COLOR_HOVER_ICON;
	private static final ImageIcon NO_BORDER_COLOR_ICON;
	private static final ImageIcon NO_BORDER_COLOR_HOVER_ICON;

	private static final ImageIcon VISIBLE_ICON;
	private static final ImageIcon VISIBLE_HOVER_ICON;
	private static final ImageIcon INVISIBLE_ICON;
	private static final ImageIcon INVISIBLE_HOVER_ICON;

	private static final ImageIcon DELETE_ICON;
	private static final ImageIcon DELETE_HOVER_ICON;

	private static final ImageIcon COLLAPSE_ICON;
	private static final ImageIcon COLLAPSE_HOVER_ICON;
	private static final ImageIcon EXPAND_ICON;
	private static final ImageIcon EXPAND_HOVER_ICON;

	private final JPanel containerSpawn = new JPanel(new BorderLayout());
	private final JPanel containerWander = new JPanel(new BorderLayout());
	private final JPanel containerMax = new JPanel(new BorderLayout());
	private final JPanel containerAggression = new JPanel(new BorderLayout());
	private final JPanel containerRetreatInteraction = new JPanel(new BorderLayout());
	private final JPanel containerNpcId = new JPanel(new BorderLayout());
	private final JPanel containerAttack = new JPanel(new BorderLayout());
	private final JPanel containerHunt = new JPanel(new BorderLayout());
	private final JPanel containerInteraction = new JPanel(new BorderLayout());
	private final JLabel colourSpawn = new JLabel();
	private final JLabel colourWander = new JLabel();
	private final JLabel colourMax = new JLabel();
	private final JLabel colourAggression = new JLabel();
	private final JLabel colourRetreatInteraction = new JLabel();
	private final JLabel colourAttack = new JLabel();
	private final JLabel colourHunt = new JLabel();
	private final JLabel colourInteraction = new JLabel();
	private final JLabel visibilityMarker = new JLabel();
	private final JLabel visibilitySpawn = new JLabel();
	private final JLabel visibilityWander = new JLabel();
	private final JLabel visibilityMax = new JLabel();
	private final JLabel visibilityAggression = new JLabel();
	private final JLabel visibilityRetreatInteraction = new JLabel();
	private final JLabel visibilityAttack = new JLabel();
	private final JLabel visibilityHunt = new JLabel();
	private final JLabel visibilityInteraction = new JLabel();
	private final JLabel deleteLabel = new JLabel();
	//private final JButton expandToggle;

	private final FlatTextField nameInput = new FlatTextField();
	private final JLabel save = new JLabel("Save");
	private final JLabel cancel = new JLabel("Cancel");
	private final JLabel rename = new JLabel("Rename");

	private final JSpinner spinnerX = new JSpinner(new SpinnerNumberModel(5, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spinnerY = new JSpinner(new SpinnerNumberModel(5, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spinnerRadiusWander = new JSpinner(new SpinnerNumberModel(5, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spinnerRadiusMax = new JSpinner(new SpinnerNumberModel(5, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spinnerNpcId = new JSpinner(new SpinnerNumberModel(5, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spinnerRadiusAttack = new JSpinner(new SpinnerNumberModel(5, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spinnerRadiusHunt = new JSpinner(new SpinnerNumberModel(5, 0, Integer.MAX_VALUE, 1));
	private final JSpinner spinnerRadiusInteraction = new JSpinner(new SpinnerNumberModel(5, 0, Integer.MAX_VALUE, 1));

	static
	{
		final BufferedImage borderImg = ImageUtil.loadImageResource(WildernessLinesPlugin.class, "border_color_icon.png");
		final BufferedImage borderImgHover = ImageUtil.luminanceOffset(borderImg, -150);
		BORDER_COLOR_ICON = new ImageIcon(borderImg);
		BORDER_COLOR_HOVER_ICON = new ImageIcon(borderImgHover);

		NO_BORDER_COLOR_ICON = new ImageIcon(borderImgHover);
		NO_BORDER_COLOR_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(borderImgHover, -100));

		final BufferedImage visibleImg = ImageUtil.loadImageResource(WildernessLinesPlugin.class, "visible_icon.png");
		VISIBLE_ICON = new ImageIcon(visibleImg);
		VISIBLE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(visibleImg, -100));

		final BufferedImage invisibleImg = ImageUtil.loadImageResource(WildernessLinesPlugin.class, "invisible_icon.png");
		INVISIBLE_ICON = new ImageIcon(invisibleImg);
		INVISIBLE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(invisibleImg, -100));

		final BufferedImage deleteImg = ImageUtil.loadImageResource(WildernessLinesPlugin.class, "delete_icon.png");
		DELETE_ICON = new ImageIcon(deleteImg);
		DELETE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(deleteImg, -100));

		BufferedImage retractIcon = ImageUtil.loadImageResource(WildernessLinesPlugin.class, "arrow_right.png");
		retractIcon = ImageUtil.luminanceOffset(retractIcon, -121);
		EXPAND_ICON = new ImageIcon(retractIcon);
		EXPAND_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(retractIcon, -100));
		final BufferedImage expandIcon = ImageUtil.rotateImage(retractIcon, Math.PI / 2);
		COLLAPSE_ICON = new ImageIcon(expandIcon);
		COLLAPSE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(expandIcon, -100));
	}

	 */
}
