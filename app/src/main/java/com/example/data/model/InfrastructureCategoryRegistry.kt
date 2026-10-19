package com.example.data.model

object InfrastructureCategoryRegistry {

  val allCategories: List<InfrastructureCategory> = listOf(
    // 1. Roads and streets
    InfrastructureCategory(
      id = "roads_streets",
      name = "Roads & Streets",
      groupName = "Transport & Streets",
      subcategories = listOf("Potholes", "Damaged roads", "Bridges", "Culverts", "Missing/damaged traffic signs"),
      defaultInstitutionId = "nra",
      defaultInstitutionName = "National Roads Authority (NRA)",
      iconKey = "AltRoute",
      colorHex = 0xFF475569
    ),

    // 2. Public transport and road safety
    InfrastructureCategory(
      id = "public_transport_safety",
      name = "Public Transport & Road Safety",
      groupName = "Transport & Streets",
      subcategories = listOf("Defective streetlights", "Damaged bus stops", "Broken traffic signals", "Unmarked/hazardous crossings"),
      defaultInstitutionId = "nra_police",
      defaultInstitutionName = "NRA / Police Traffic Directorate",
      iconKey = "DirectionsBus",
      colorHex = 0xFF2563EB
    ),

    // 3. Drainage and flooding
    InfrastructureCategory(
      id = "drainage_flooding",
      name = "Drainage & Flooding",
      groupName = "Sanitation & Water",
      subcategories = listOf("Blocked drains & culverts", "Damaged gutters", "Flooded streets & roads"),
      defaultInstitutionId = "nra_kmc",
      defaultInstitutionName = "National Roads Authority & Municipal Engineering",
      iconKey = "Waves",
      colorHex = 0xFF0284C7
    ),

    // 4. Waste management
    InfrastructureCategory(
      id = "waste_management",
      name = "Waste Management",
      groupName = "Sanitation & Water",
      subcategories = listOf("Illegal dumping sites", "Uncollected domestic waste", "Overflowing public bins", "Hazardous market refuse"),
      defaultInstitutionId = "kmc",
      defaultInstitutionName = "Kanifing Municipal Council (KMC) / Local Councils",
      iconKey = "DeleteOutline",
      colorHex = 0xFF0F6B38
    ),

    // 5. Water and sewerage
    InfrastructureCategory(
      id = "water_sewerage",
      name = "Water & Sewerage",
      groupName = "Utilities & Energy",
      subcategories = listOf("Burst water main pipes", "Community water shortages", "Raw sewage overflow", "Water contamination"),
      defaultInstitutionId = "nawec",
      defaultInstitutionName = "NAWEC (Water & Sewerage Directorate)",
      iconKey = "WaterDrop",
      colorHex = 0xFF0284C7
    ),

    // 6. Electricity
    InfrastructureCategory(
      id = "electricity",
      name = "Electricity & Power",
      groupName = "Utilities & Energy",
      subcategories = listOf("Damaged utility poles", "Exposed live wires", "Faulty transformers", "Public lighting network outages"),
      defaultInstitutionId = "nawec",
      defaultInstitutionName = "NAWEC (Electricity Directorate)",
      iconKey = "Bolt",
      colorHex = 0xFFD97706
    ),

    // 7. Telecommunications
    InfrastructureCategory(
      id = "telecom",
      name = "Telecommunications",
      groupName = "Utilities & Energy",
      subcategories = listOf("Damaged overhead fiber cables", "Fallen telecom poles", "Public digital kiosks", "Tower coverage blackouts"),
      defaultInstitutionId = "pura",
      defaultInstitutionName = "PURA / Mobile Network Operators",
      iconKey = "CellTower",
      colorHex = 0xFF7C3AED
    ),

    // 8. Education
    InfrastructureCategory(
      id = "education",
      name = "Education Infrastructure",
      groupName = "Civic & Public Facilities",
      subcategories = listOf("Damaged school classrooms", "Broken school toilets", "Collapsed school fences", "School borehole water points"),
      defaultInstitutionId = "mobse",
      defaultInstitutionName = "Ministry of Basic & Secondary Education (MoBSE)",
      iconKey = "School",
      colorHex = 0xFF16A34A
    ),

    // 9. Healthcare
    InfrastructureCategory(
      id = "healthcare",
      name = "Healthcare Infrastructure",
      groupName = "Civic & Public Facilities",
      subcategories = listOf("Damaged health centres & clinics", "Hospital wards & roofs", "Essential cold chain & clinic power", "Ambulance access paths"),
      defaultInstitutionId = "moh",
      defaultInstitutionName = "Ministry of Health (MoH)",
      iconKey = "LocalHospital",
      colorHex = 0xFFDC2626
    ),

    // 10. Markets and public buildings
    InfrastructureCategory(
      id = "markets_buildings",
      name = "Markets & Public Buildings",
      groupName = "Civic & Public Facilities",
      subcategories = listOf("Damaged market stalls", "Leaking market shed roofs", "Unsanitary market toilets", "Damaged community centres"),
      defaultInstitutionId = "kmc",
      defaultInstitutionName = "Municipal Councils (KMC / BCC / BAC / Area Councils)",
      iconKey = "Storefront",
      colorHex = 0xFFEA580C
    ),

    // 11. Parks and recreation
    InfrastructureCategory(
      id = "parks_recreation",
      name = "Parks & Recreation",
      groupName = "Civic & Public Facilities",
      subcategories = listOf("Broken playground equipment", "Damaged sports grounds & mini-stadiums", "Public benches & recreation spaces"),
      defaultInstitutionId = "kmc_parks",
      defaultInstitutionName = "Municipal Parks & Community Directorate",
      iconKey = "Park",
      colorHex = 0xFF059669
    ),

    // 12. Coastal protection
    InfrastructureCategory(
      id = "coastal_protection",
      name = "Coastal Protection",
      groupName = "Environment & Natural Hazards",
      subcategories = listOf("Beach & shoreline erosion", "Damaged sea defences & revetments", "Sand dunes breach"),
      defaultInstitutionId = "nea",
      defaultInstitutionName = "National Environment Agency (NEA) Coastal Unit",
      iconKey = "Sailing",
      colorHex = 0xFF0891B2
    ),

    // 13. Environmental hazards
    InfrastructureCategory(
      id = "environmental_hazards",
      name = "Environmental Hazards",
      groupName = "Environment & Natural Hazards",
      subcategories = listOf("Industrial waste pollution", "Oil & fuel spills", "Bushfires & dry brush hazard", "Fallen hazardous trees"),
      defaultInstitutionId = "nea",
      defaultInstitutionName = "National Environment Agency (NEA) & Forestry",
      iconKey = "Forest",
      colorHex = 0xFF15803D
    ),

    // 14. Emergency infrastructure
    InfrastructureCategory(
      id = "emergency_infrastructure",
      name = "Emergency Infrastructure",
      groupName = "Safety & Emergency",
      subcategories = listOf("Damaged/inoperable fire hydrants", "Blocked emergency evacuation routes", "Civic emergency shelters"),
      defaultInstitutionId = "fire_service",
      defaultInstitutionName = "The Gambia Fire & Rescue Services / NDMA",
      iconKey = "FireHydrantAlt",
      colorHex = 0xFFB91C1C
    ),

    // 15. Public safety
    InfrastructureCategory(
      id = "public_safety",
      name = "Public Safety Hazards",
      groupName = "Safety & Emergency",
      subcategories = listOf("Open/uncovered manholes", "Collapsing perimeter walls", "Dangerous unbarricaded excavations", "Exposed sinkholes"),
      defaultInstitutionId = "police_force",
      defaultInstitutionName = "The Gambia Police Force & Municipal Ward Safety",
      iconKey = "Shield",
      colorHex = 0xFF4338CA
    ),

    // 16. Disaster preparedness
    InfrastructureCategory(
      id = "disaster_preparedness",
      name = "Disaster Preparedness",
      groupName = "Safety & Emergency",
      subcategories = listOf("Flood-prone residential hotspots", "Breached storm embankments", "Community evacuation transit points"),
      defaultInstitutionId = "ndma",
      defaultInstitutionName = "National Disaster Management Agency (NDMA)",
      iconKey = "WarningAmber",
      colorHex = 0xFFC2410C
    ),

    // 17. Ferries and river transport
    InfrastructureCategory(
      id = "ferries_river",
      name = "Ferries & River Transport",
      groupName = "Ports, Maritime & Aviation",
      subcategories = listOf("Damaged landing jetties", "Ferry terminal waiting facilities", "Ramp docking defects", "Mooring safety bollards"),
      defaultInstitutionId = "gambia_ferries",
      defaultInstitutionName = "Gambia Ferry Services (GPA)",
      iconKey = "DirectionsBoat",
      colorHex = 0xFF1E40AF
    ),

    // 18. Ports and aviation
    InfrastructureCategory(
      id = "ports_aviation",
      name = "Ports & Aviation",
      groupName = "Ports, Maritime & Aviation",
      subcategories = listOf("Damaged seaport public-access", "Cargo gate access roads", "Airport perimeter security fencing & drainage"),
      defaultInstitutionId = "gpa",
      defaultInstitutionName = "Gambia Ports Authority (GPA) & GCAA",
      iconKey = "FlightTakeoff",
      colorHex = 0xFF312E81
    ),

    // 19. Accessibility
    InfrastructureCategory(
      id = "accessibility",
      name = "Accessibility Infrastructure",
      groupName = "Inclusive Community",
      subcategories = listOf("Missing wheelchair ramps at civic offices", "Blocked pedestrian footpaths", "Unsafe street crossings for disabled"),
      defaultInstitutionId = "social_welfare",
      defaultInstitutionName = "Department of Social Welfare & Municipal Works",
      iconKey = "Accessible",
      colorHex = 0xFF0D9488
    ),

    // 20. Rural infrastructure
    InfrastructureCategory(
      id = "rural_infrastructure",
      name = "Rural Infrastructure",
      groupName = "Rural & Primary Industries",
      subcategories = listOf("Rural feeder roads", "Village timber/concrete footbridges", "Solar communal boreholes & water points"),
      defaultInstitutionId = "mrg",
      defaultInstitutionName = "Ministry of Regional Lands & Civic Affairs (MRG)",
      iconKey = "Terrain",
      colorHex = 0xFF65A30D
    ),

    // 21. Agriculture
    InfrastructureCategory(
      id = "agriculture",
      name = "Agriculture Infrastructure",
      groupName = "Rural & Primary Industries",
      subcategories = listOf("Damaged irrigation channels", "Agricultural solar pumps", "Tidal irrigation dykes", "Grain storage sheds"),
      defaultInstitutionId = "moa",
      defaultInstitutionName = "Ministry of Agriculture (MoA) Engineering Unit",
      iconKey = "Agriculture",
      colorHex = 0xFF4D7C0F
    ),

    // 22. Fisheries
    InfrastructureCategory(
      id = "fisheries",
      name = "Fisheries Infrastructure",
      groupName = "Rural & Primary Industries",
      subcategories = listOf("Damaged fish landing sites", "Fish smoking & processing huts", "Ice plant & cold storage facilities"),
      defaultInstitutionId = "fisheries_dept",
      defaultInstitutionName = "Ministry of Fisheries & Water Resources",
      iconKey = "Phishing",
      colorHex = 0xFF0369A1
    )
  )

  // Gambian Administrative Regional Hierarchy
  val gambianRegions: List<GambianRegion> = listOf(
    GambianRegion(
      id = "gba",
      name = "Greater Banjul Area (GBA)",
      districts = listOf(
        GambianDistrict(
          id = "kmc_dist",
          name = "Kanifing Municipality",
          communities = listOf("Serrekunda Central", "Bakau", "Fajara", "Tallinding", "Latrikunda Sabiji", "Bundung", "Manjai Kunda", "Old Jeshwang")
        ),
        GambianDistrict(
          id = "banjul_dist",
          name = "Banjul City",
          communities = listOf("Albert Market Sector", "Half Die", "Campama", "Portuguese Town", "Ferry Terminal Quarter")
        )
      )
    ),
    GambianRegion(
      id = "wcr",
      name = "West Coast Region (WCR)",
      districts = listOf(
        GambianDistrict(
          id = "kombo_north",
          name = "Kombo North",
          communities = listOf("Brusubi", "Sukuta", "Bijilo", "Kerr Sering", "Wulinkama", "Lamin")
        ),
        GambianDistrict(
          id = "kombo_central",
          name = "Kombo Central",
          communities = listOf("Brikama Central", "Brikama Box Bar", "Marakissa", "Nyambai")
        ),
        GambianDistrict(
          id = "kombo_south",
          name = "Kombo South",
          communities = listOf("Gunjur", "Sanyang", "Tanji", "Kartong", "Batokunku")
        )
      )
    ),
    GambianRegion(
      id = "nbr",
      name = "North Bank Region (NBR)",
      districts = listOf(
        GambianDistrict(
          id = "lower_niumi",
          name = "Lower Niumi",
          communities = listOf("Barra Ferry Sector", "Essau", "Kanuma", "Mayamba")
        ),
        GambianDistrict(
          id = "upper_badibou",
          name = "Upper Badibou",
          communities = listOf("Farafenni Central", "Ngayen Sanjal", "Ballanghar")
        )
      )
    ),
    GambianRegion(
      id = "lrr",
      name = "Lower River Region (LRR)",
      districts = listOf(
        GambianDistrict(
          id = "jarra_west",
          name = "Jarra West",
          communities = listOf("Soma Commercial Crossroads", "Mansakonko", "Karantaba")
        ),
        GambianDistrict(
          id = "kiang_central",
          name = "Kiang Central",
          communities = listOf("Kwinella", "Batelling", "Nema Kuta")
        )
      )
    ),
    GambianRegion(
      id = "crr",
      name = "Central River Region (CRR)",
      districts = listOf(
        GambianDistrict(
          id = "janjanbureh_dist",
          name = "Janjanbureh District",
          communities = listOf("Janjanbureh Island", "Lamin Koto Landing", "Georgetown South")
        ),
        GambianDistrict(
          id = "fulladu_west",
          name = "Fulladu West",
          communities = listOf("Brikama Ba", "Bansang", "Sankulay Kunda")
        )
      )
    ),
    GambianRegion(
      id = "urr",
      name = "Upper River Region (URR)",
      districts = listOf(
        GambianDistrict(
          id = "basse_dist",
          name = "Basse District",
          communities = listOf("Basse Santa Su", "Basse Koba Kunda", "Mansajang Kunda")
        ),
        GambianDistrict(
          id = "wuli_west",
          name = "Wuli West",
          communities = listOf("Yorobawol", "Sutukoba", "Taifa")
        )
      )
    )
  )

  // Default initial configurable routing rules
  fun createInitialRoutingRules(): List<InstitutionalRoutingRule> {
    return allCategories.map { cat ->
      val sla = when (cat.id) {
        "electricity", "water_sewerage", "emergency_infrastructure", "public_safety" -> 24
        "roads_streets", "drainage_flooding", "waste_management" -> 48
        "healthcare", "education" -> 36
        else -> 72
      }
      InstitutionalRoutingRule(
        id = "rule_${cat.id}",
        categoryId = cat.id,
        categoryName = cat.name,
        subcategoryFilter = null,
        targetInstitutionId = cat.defaultInstitutionId,
        targetInstitutionName = cat.defaultInstitutionName,
        prioritySlaHours = sla,
        autoEscalateUrgent = true,
        notificationEmail = "dispatch@${cat.defaultInstitutionId}.gov.gm"
      )
    }
  }

  fun mapCategoryIdToIssueCategory(categoryId: String): IssueCategory {
    return when (categoryId) {
      "roads_streets", "public_transport_safety" -> IssueCategory.ROADS
      "drainage_flooding", "water_sewerage" -> IssueCategory.WATER
      "waste_management" -> IssueCategory.WASTE
      "electricity" -> IssueCategory.ELECTRICITY
      "telecom" -> IssueCategory.GSM
      "healthcare", "education", "environmental_hazards" -> IssueCategory.HEALTH
      "emergency_infrastructure", "public_safety", "disaster_preparedness" -> IssueCategory.SECURITY
      else -> IssueCategory.OTHER
    }
  }
}
