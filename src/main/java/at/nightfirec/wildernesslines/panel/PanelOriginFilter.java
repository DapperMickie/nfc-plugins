package at.nightfirec.wildernesslines.panel;

public enum PanelOriginFilter
{
	ALL,
	PLUGIN,
	CUSTOM;

	private static final PanelOriginFilter[] FILTERS = values();

	public PanelOriginFilter next()
	{
		return FILTERS[(this.ordinal() + 1) % FILTERS.length];
	}
}
