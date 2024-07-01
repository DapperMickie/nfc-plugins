package at.nightfirec.wildernesslines;

enum PanelFilter
{
	ALL,
	PLUGIN,
	CUSTOM;

	private static final PanelFilter[] FILTERS = values();

	public PanelFilter next()
	{
		return FILTERS[(this.ordinal() + 1) % FILTERS.length];
	}
}