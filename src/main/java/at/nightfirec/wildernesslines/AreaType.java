package at.nightfirec.wildernesslines;

public enum AreaType
{
	// All multi areas in wilderness have this type
	WILDERNESS_MULTI,
	// All 20 lines in wilderness will have this type
	WILDERNESS_20_LINE,
	// ALL 30 lines in wilderness will have this type
	WILDERNESS_30_LINE,
	// All unsafe multi areas that aren't in wilderness have this type
	WORLD_MULTI,
	// Safe zone in PVP worlds
	SAFE_ZONE,
	// Guarded area in DMM worlds
	GUARDED_AREA
}
