package at.nightfirec.wildernesslines;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.Varbits;
import net.runelite.api.WorldType;
import net.runelite.api.WorldView;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class CoordinateSidePanel extends PluginPanel
{
	private final Client client;
	private final WildernessLinesConfig config;
	private final JLabel worldTypeLabel;
	private final JLabel xCoordinateLabel;
	private final JLabel yCoordinateLabel;
	private final JLabel worldViewLabel;
	private final JLabel planeLabel;
	private final JLabel multiCombatLabel;
	private final JTextField outputField;
	private final JButton setSouthWestXButton;
	private final JButton setSouthWestYButton;
	private final JButton setNorthEastXButton;
	private final JButton setNorthEastYButton;
	private final JButton resetTrackingButton;
	private final JButton calculateButton;
	private final JButton showLocationButton;
	private final WildernessLinesPlugin plugin;

	private final JTextField x1Field, y1Field, x2Field, y2Field;

	private int x1, y1, x2, y2, width, height;

	@Inject
	public CoordinateSidePanel(Client client, WildernessLinesConfig config, WildernessLinesPlugin plugin)
	{
		this.client = client;
		this.config = config;
		this.plugin = plugin;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.DARK_GRAY);

		add(new JLabel("Wilderness Lines Devtool"));

		//createLabel("<html><br />To start tracking, make sure to go to the most South-Western tile of the area you want to add.<br /><br />Once you're there, click on the 'South-West Tile' button.<br /><br />The second step is to go to the most northern tile of the area you want to add, then once you're there, click the 'North Tile' button.<br /><br />The third step is to go to the most eastern tile and onc you're there, click the 'East Tile' button.<br /><br />Once you've done all of that, click on the 'Done Tracking' button and copy the output.<br /><br />Create a new Issue on the GitHub repo of this project, please include the name of the area and the world type you're in (click the 'Copy World Types' button).<br /><br /></html>");

		worldTypeLabel = createLabel("World Type: ");
		xCoordinateLabel = createLabel("X: ");
		yCoordinateLabel = createLabel("Y: ");
		worldViewLabel = createLabel("WorldView: ");
		planeLabel = createLabel("Plane: ");
		multiCombatLabel = createLabel("In Multi (1t delay): ");

		createLabel("South-West X:");
		x1Field = new JTextField();
		add(x1Field);

		setSouthWestXButton = new JButton("Set South-West X");
		setSouthWestXButton.addActionListener(e -> setX1());
		add(setSouthWestXButton);

		createLabel("South-West Y:");
		y1Field = new JTextField();
		add(y1Field);

		setSouthWestYButton = new JButton("Set South-West Y");
		setSouthWestYButton.addActionListener(e -> setY1());
		add(setSouthWestYButton);

		createLabel("North-East X:");
		x2Field = new JTextField();
		add(x2Field);

		setNorthEastXButton = new JButton("Set North-East X");
		setNorthEastXButton.addActionListener(e -> setX2());
		add(setNorthEastXButton);

		createLabel("North-East Y:");
		y2Field = new JTextField();
		add(y2Field);

		setNorthEastYButton = new JButton("Set North-East Y");
		setNorthEastYButton.addActionListener(e -> setY2());
		add(setNorthEastYButton);

		calculateButton = new JButton("Calculate");
		calculateButton.addActionListener(e -> calculate());
		calculateButton.setEnabled(false);
		add(calculateButton);

		showLocationButton = new JButton("Show Location");
		showLocationButton.setEnabled(false);
		showLocationButton.addActionListener(e -> showLocation());
		add(showLocationButton);

		JButton copyButton = new JButton("Copy");
		copyButton.setEnabled(true);
		copyButton.addActionListener(e -> copyOutput());
		add(copyButton);

		outputField = new JTextField();
		outputField.setEditable(false);
		outputField.setText("No output...");
		add(outputField);

		resetTrackingButton = new JButton("Reset Tracking");
		resetTrackingButton.setEnabled(false);
		resetTrackingButton.addActionListener(e -> resetTracking());
		add(resetTrackingButton);
	}

	private JLabel createLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setForeground(Color.WHITE);
		add(label);
		return label;
	}

	public void update()
	{
		if (client.getLocalPlayer() == null)
		{
			return;
		}

		Player localPlayer = client.getLocalPlayer();
		WorldPoint worldPoint = localPlayer.getWorldLocation();
		WorldView worldView = localPlayer.getWorldView();

		StringBuilder worldTypeNames = new StringBuilder();
		worldTypeNames.append("<html>World Type: ");
		for (WorldType type : client.getWorldType())
		{
			worldTypeNames.append("<br />");
			worldTypeNames.append(type.name());
		}
		worldTypeNames.append("<br /><br /></html>");
		worldTypeLabel.setText(String.valueOf(worldTypeNames));
		xCoordinateLabel.setText("X: " + worldPoint.getX());
		yCoordinateLabel.setText("Y: " + worldPoint.getY());
		worldViewLabel.setText("WorldView: " + worldView.getId());
		planeLabel.setText("Plane: " + worldView.getPlane());
		multiCombatLabel.setText("In Multi (1t delay): " + (client.getVarbitValue(Varbits.MULTICOMBAT_AREA) == 0 ? "No" : "Yes"));
	}

	private void setX1()
	{
		try
		{
			x1 = Integer.parseInt(x1Field.getText());
			cheackIfReadyToAdd();
		}
		catch (NumberFormatException ignored)
		{
		}
	}

	private void setX2()
	{
		try
		{
			x2 = Integer.parseInt(x2Field.getText());
			cheackIfReadyToAdd();
		}
		catch (NumberFormatException ignored)
		{
		}
	}

	private void setY1()
	{
		try
		{
			y1 = Integer.parseInt(y1Field.getText());
			cheackIfReadyToAdd();
		}
		catch (NumberFormatException ignored)
		{
		}
	}

	private void setY2()
	{
		try
		{
			y2 = Integer.parseInt(y2Field.getText());
			cheackIfReadyToAdd();
		}
		catch (NumberFormatException ignored)
		{
		}
	}

	private void resetTracking()
	{
		x1 = y1 = x2 = y2 = width = height = 0;
		outputField.setText("No output...");

		setNorthEastXButton.setEnabled(true);
		setNorthEastYButton.setEnabled(true);
		setSouthWestXButton.setEnabled(true);
		setSouthWestYButton.setEnabled(true);
		resetTrackingButton.setEnabled(false);
		calculateButton.setEnabled(false);
	}

	private void cheackIfReadyToAdd()
	{
		if (x1 <= 0 || x2 <= 0 || y1 <= 0 || y2 <= 0)
		{
			return;
		}

		calculateButton.setEnabled(true);
	}

	/*
	private void measureHeight()
	{
		if (!tracking)
		{
			return;
		}

		Player localPlayer = client.getLocalPlayer();
		if (localPlayer == null)
		{
			return;
		}

		WorldPoint worldPoint = localPlayer.getWorldLocation();
		height = worldPoint.getY() - y1 + 1;
		checkIfReadyToAdd();
	}

	private void measureWidth()
	{
		if (!tracking)
		{
			return;
		}

		Player localPlayer = client.getLocalPlayer();
		if (localPlayer == null)
		{
			return;
		}

		WorldPoint worldPoint = localPlayer.getWorldLocation();
		width = Math.abs(worldPoint.getX() - x1) + 1;
		checkIfReadyToAdd();
	}
	*/


	private void copyOutput()
	{
		String outputFieldText = outputField.getText();
		if (outputFieldText.equals("No output..."))
		{
			return;
		}

		StringBuilder outputStringBuilder = new StringBuilder();

		outputStringBuilder.append("Here is all the information for area: AREA_NAME_HERE\n");
		outputStringBuilder.append(worldViewLabel.getText());
		outputStringBuilder.append("\n");
		outputStringBuilder.append(planeLabel.getText());
		outputStringBuilder.append("\n");
		outputStringBuilder.append("World Types: ");
		outputStringBuilder.append(getWorldTypeNames());
		outputStringBuilder.append("\nTo Add: \n");
		outputStringBuilder.append(outputFieldText);

		copy(String.valueOf(outputStringBuilder));
	}

	private String getWorldTypeNames()
	{
		StringBuilder worldTypeNames = new StringBuilder();
		boolean isNotFirst = false;
		for (WorldType type : client.getWorldType())
		{
			if (isNotFirst)
			{
				worldTypeNames.append(", ");
			}
			worldTypeNames.append(type.name());
			isNotFirst = true;
		}
		return String.valueOf(worldTypeNames);
	}

	private void showLocation()
	{
		plugin.addLocation(new Rectangle(x1, y1, width, height));
	}

	private void copy(String text)
	{
		StringSelection stringSelection = new StringSelection(text);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

	private void calculate()
	{
		if (x1 <= 0 || x2 <= 0 || y1 <= 0 || y2 <= 0)
		{
			return;
		}

		height = y2 - y1 + 1;
		width = x2 - x1 + 1;

		String output = String.format("new Rectangle(%d, %d, %d, %d)", x1, y1, width, height);
		outputField.setText(output);

		showLocationButton.setEnabled(true);
	}
}
