package at.nightfirec.wildernesslines;

import java.awt.geom.Line2D;

public class PluginAreaGroups
{
	private static final int DEFAULT_PLANE = 0;
	private static final int DEFAULT_WORLDVIEW_ID = -1;

	private static final AreaGroup WILDERNESS_AREA_GROUP = new AreaGroup("Wilderness",
		AreaOrigin.PLUGIN,
		AreaType.WILDERNESS_MULTI,
		new Area[]{
			new Area(3008, 3600, 3071, 3711, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // Dark warrior's palace
			new Area(3072, 3654, 3072, 3655, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // Two tiles next to southern rev caves entrance which used to be a BH "singles" lure spot
			new Area(2946, 3816, 2959, 3831, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // Chaos altar
			new Area(2984, 3912, 3007, 3927, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // Balance crossing to wilderness agility course
			new Area(3008, 3856, 3047, 3903, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // North of kbd entrance
			new Area(3021, 3855, 3022, 3855, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // Two tiles NE of kbd entrance cage
			new Area(3048, 3896, 3071, 3903, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // North of rune rocks
			new Area(3072, 3880, 3135, 3903, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // North of lava maze
			new Area(3112, 3872, 3135, 3879, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // Northeast of lava maze
			new Area(3136, 3840, 3391, 3903, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // Northeast f2p wilderness
			new Area(3200, 3904, 3391, 3967, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // Northeast p2p wilderness
			new Area(3152, 3752, 3327, 3840, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // North-mid east f2p wilderness
			new Area(3192, 3525, 3327, 3751, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // East f2p wilderness
			new Area(3328, 3525, 3344, 3542, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // Small east f2p wilderness strip NE of lumberyard
			new Area(3191, 3689, 3191, 3689, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // One silly tile that used to be a BH "singles" lure spot
			new Area(3136, 3525, 3191, 3583, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // Wilderness north of Grand Exchange
			new Area(3152, 3584, 3191, 3619, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // SE of Ferox 1
			new Area(3146, 3598, 3151, 3619, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // SE of Ferox 2
			new Area(3147, 3596, 3151, 3597, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // SE of Ferox 2 extension 1
			new Area(3149, 3595, 3151, 3595, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // SE of Ferox 2 extension 2
			new Area(3150, 3594, 3151, 3594, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // SE of Ferox 2 extension 3
			new Area(3151, 3593, 3151, 3593, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // SE of Ferox 2 extension 4
			new Area(3152, 3620, 3161, 3625, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // SE of Ferox 3
			new Area(3187, 3620, 3191, 3647, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // East of Ferox 1
			new Area(3176, 3636, 3186, 3647, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID), // East of Ferox 2
			new Area(3175, 3647, 3175, 3647, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID)  // One dumb tile north of bridge east of Ferox
		});

	private final static AreaGroup WILDERNESS_20_LINES = new AreaGroup("Wilderness 20 Lines",
		AreaOrigin.PLUGIN,
		AreaType.WILDERNESS_20_LINE,
		new Area[]{
			// overworld
			new Area(2946, 3680, 3384, 3680, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),

			// rev caves
			new Area(3202, 10080, 3205, 10080, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
			new Area(3216, 10080, 3224, 10080, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
			new Area(3228, 10080, 3230, 10080, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
			new Area(3234, 10080, 3245, 10080, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),

			// wilderness slayer caves
			new Area(3335, 10080, 3344, 10080, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
			new Area(3349, 10080, 3367, 10080, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
			new Area(3381, 10080, 3385, 10080, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
			new Area(3394, 10080, 3397, 10080, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
			new Area(3410, 10080, 3416, 10080, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
			new Area(3436, 10080, 3449, 10080, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
		});

	private final static AreaGroup WILDERNESS_30_LINES = new AreaGroup("Wilderness 30 Lines",
		AreaOrigin.PLUGIN,
		AreaType.WILDERNESS_30_LINE,
		new Area[]{
			// overworld
			new Area(2946, 3760, 3375, 3760, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),

			// rev caves
			new Area(3164, 10160, 3185, 10160, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
			new Area(3194, 10160, 3221, 10160, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
			new Area(3235, 10160, 3255, 10160, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),

			// wilderness slayer caves
			new Area(3333, 10160, 3349, 10160, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
			new Area(3356, 10160, 3368, 10160, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
			new Area(3421, 10160, 3428, 10160, DEFAULT_PLANE, DEFAULT_WORLDVIEW_ID),
		});

	public static AreaGroup getWildernessAreaGroup()
	{
		return WILDERNESS_AREA_GROUP;
	}

	public static AreaGroup getWilderness20Lines()
	{
		return WILDERNESS_20_LINES;
	}

	public static AreaGroup getWilderness30Lines()
	{
		return WILDERNESS_30_LINES;
	}
}
