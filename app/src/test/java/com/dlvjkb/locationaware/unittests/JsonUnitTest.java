package com.dlvjkb.locationaware.unittests;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUnitTest {

    public JSONObject getResponseJson() throws JSONException {
        String responseJson = "{\n" +
                "    \"type\": \"FeatureCollection\",\n" +
                "    \"features\": [\n" +
                "        {\n" +
                "            \"bbox\": [\n" +
                "                8.681436,\n" +
                "                49.41461,\n" +
                "                8.690123,\n" +
                "                49.420514\n" +
                "            ],\n" +
                "            \"type\": \"Feature\",\n" +
                "            \"properties\": {\n" +
                "                \"segments\": [\n" +
                "                    {\n" +
                "                        \"distance\": 887.8, \n" +
                "                        \"duration\": 189.0, \n" +
                "                        \"steps\": [ \n" +
                "                            {\n" +
                "                                \"distance\": 312.6,\n" +
                "                                \"duration\": 75.0,\n" +
                "                                \"type\": 11,\n" +
                "                                \"instruction\": \"Head north on Wielandtstraße\",\n" +
                "                                \"name\": \"Wielandtstraße\",\n" +
                "                                \"way_points\": [\n" +
                "                                    0,\n" +
                "                                    10\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"distance\": 251.1,\n" +
                "                                \"duration\": 36.2,\n" +
                "                                \"type\": 1,\n" +
                "                                \"instruction\": \"Turn right onto Mönchhofstraße\",\n" +
                "                                \"name\": \"Mönchhofstraße\",\n" +
                "                                \"way_points\": [\n" +
                "                                    10,\n" +
                "                                    21\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"distance\": 212.2,\n" +
                "                                \"duration\": 50.9,\n" +
                "                                \"type\": 2,\n" +
                "                                \"instruction\": \"Turn sharp left onto Keplerstraße\",\n" +
                "                                \"name\": \"Keplerstraße\",\n" +
                "                                \"way_points\": [\n" +
                "                                    21,\n" +
                "                                    24\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"distance\": 109.9,\n" +
                "                                \"duration\": 26.4,\n" +
                "                                \"type\": 1,\n" +
                "                                \"instruction\": \"Turn right onto Moltkestraße\",\n" +
                "                                \"name\": \"Moltkestraße\",\n" +
                "                                \"way_points\": [\n" +
                "                                    24,\n" +
                "                                    27\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"distance\": 2.0,\n" +
                "                                \"duration\": 0.5,\n" +
                "                                \"type\": 0,\n" +
                "                                \"instruction\": \"Turn left onto Werderplatz\",\n" +
                "                                \"name\": \"Werderplatz\",\n" +
                "                                \"way_points\": [\n" +
                "                                    27,\n" +
                "                                    28\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"distance\": 0.0,\n" +
                "                                \"duration\": 0.0,\n" +
                "                                \"type\": 10,\n" +
                "                                \"instruction\": \"Arrive at Werderplatz, on the right\",\n" +
                "                                \"name\": \"-\",\n" +
                "                                \"way_points\": [\n" +
                "                                    28,\n" +
                "                                    28\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"distance\": 481.2,\n" +
                "                        \"duration\": 103.0,\n" +
                "                        \"steps\": [\n" +
                "                            {\n" +
                "                                \"distance\": 2.0,\n" +
                "                                \"duration\": 0.5,\n" +
                "                                \"type\": 11,\n" +
                "                                \"instruction\": \"Head south on Werderplatz\",\n" +
                "                                \"name\": \"Werderplatz\",\n" +
                "                                \"way_points\": [\n" +
                "                                    28,\n" +
                "                                    29\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"distance\": 265.5,\n" +
                "                                \"duration\": 63.7,\n" +
                "                                \"type\": 0,\n" +
                "                                \"instruction\": \"Turn left onto Moltkestraße\",\n" +
                "                                \"name\": \"Moltkestraße\",\n" +
                "                                \"way_points\": [\n" +
                "                                    29,\n" +
                "                                    37\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"distance\": 83.0,\n" +
                "                                \"duration\": 7.5,\n" +
                "                                \"type\": 0,\n" +
                "                                \"instruction\": \"Turn left onto Handschuhsheimer Landstraße, B 3\",\n" +
                "                                \"name\": \"Handschuhsheimer Landstraße, B 3\",\n" +
                "                                \"way_points\": [\n" +
                "                                    37,\n" +
                "                                    39\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"distance\": 130.8,\n" +
                "                                \"duration\": 31.4,\n" +
                "                                \"type\": 0,\n" +
                "                                \"instruction\": \"Turn left onto Roonstraße\",\n" +
                "                                \"name\": \"Roonstraße\",\n" +
                "                                \"way_points\": [\n" +
                "                                    39,\n" +
                "                                    42\n" +
                "                                ]\n" +
                "                            },\n" +
                "                            {\n" +
                "                                \"distance\": 0.0,\n" +
                "                                \"duration\": 0.0,\n" +
                "                                \"type\": 10,\n" +
                "                                \"instruction\": \"Arrive at Roonstraße, straight ahead\",\n" +
                "                                \"name\": \"-\",\n" +
                "                                \"way_points\": [\n" +
                "                                    42,\n" +
                "                                    42\n" +
                "                                ]\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"summary\": {\n" +
                "                    \"distance\": 1369.0,\n" +
                "                    \"duration\": 292.0\n" +
                "                },\n" +
                "                \"way_points\": [\n" +
                "                    0,\n" +
                "                    28,\n" +
                "                    42\n" +
                "                ]\n" +
                "            },\n" +
                "            \"geometry\": {\n" +
                "                \"coordinates\": [\n" +
                "                    [\n" +
                "                        8.681496,\n" +
                "                        49.41461\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.68149,\n" +
                "                        49.414711\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.681441,\n" +
                "                        49.415655\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.681436,\n" +
                "                        49.415747\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.681455,\n" +
                "                        49.415835\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.681509,\n" +
                "                        49.416087\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.681642,\n" +
                "                        49.416498\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.681671,\n" +
                "                        49.416588\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.681701,\n" +
                "                        49.416684\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.681875,\n" +
                "                        49.417287\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.68189,\n" +
                "                        49.417394\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.682045,\n" +
                "                        49.41739\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.682107,\n" +
                "                        49.41739\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.682461,\n" +
                "                        49.417389\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.682563,\n" +
                "                        49.417388\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.682676,\n" +
                "                        49.417387\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.682782,\n" +
                "                        49.417388\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.683371,\n" +
                "                        49.417368\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.683592,\n" +
                "                        49.41736\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.683763,\n" +
                "                        49.417362\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.685222,\n" +
                "                        49.417366\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.685359,\n" +
                "                        49.417364\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.685342,\n" +
                "                        49.417411\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.685024,\n" +
                "                        49.419178\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.68501,\n" +
                "                        49.419258\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.685156,\n" +
                "                        49.419273\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.686408,\n" +
                "                        49.419402\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.68651,\n" +
                "                        49.419413\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.686506,\n" +
                "                        49.41943\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.68651,\n" +
                "                        49.419413\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.686617,\n" +
                "                        49.419425\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.686983,\n" +
                "                        49.419461\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.687101,\n" +
                "                        49.419473\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.687212,\n" +
                "                        49.419486\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.688301,\n" +
                "                        49.419619\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.688398,\n" +
                "                        49.41963\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.690104,\n" +
                "                        49.419828\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.690123,\n" +
                "                        49.419833\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.689854,\n" +
                "                        49.420216\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.689652,\n" +
                "                        49.420514\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.68963,\n" +
                "                        49.42051\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.688601,\n" +
                "                        49.420393\n" +
                "                    ],\n" +
                "                    [\n" +
                "                        8.687872,\n" +
                "                        49.420318\n" +
                "                    ]\n" +
                "                ],\n" +
                "                \"type\": \"LineString\"\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"bbox\": [\n" +
                "        8.681436,\n" +
                "        49.41461,\n" +
                "        8.690123,\n" +
                "        49.420514\n" +
                "    ],\n" +
                "    \"metadata\": {\n" +
                "        \"attribution\": \"openrouteservice.org | OpenStreetMap contributors\",\n" +
                "        \"service\": \"routing\",\n" +
                "        \"timestamp\": 1610101322341,\n" +
                "        \"query\": {\n" +
                "            \"coordinates\": [\n" +
                "                [\n" +
                "                    8.681495,\n" +
                "                    49.41461\n" +
                "                ],\n" +
                "                [\n" +
                "                    8.686507,\n" +
                "                    49.41943\n" +
                "                ],\n" +
                "                [\n" +
                "                    8.687872,\n" +
                "                    49.420318\n" +
                "                ]\n" +
                "            ],\n" +
                "            \"profile\": \"driving-car\",\n" +
                "            \"format\": \"geojson\"\n" +
                "        },\n" +
                "        \"engine\": {\n" +
                "            \"version\": \"6.3.1\",\n" +
                "            \"build_date\": \"2020-12-10T15:35:43Z\",\n" +
                "            \"graph_date\": \"1970-01-01T00:00:00Z\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        //initialize response json
        JSONObject routeJson = new JSONObject(responseJson);
        return routeJson;
    }

    public ArrayList<String> getLocationList(){
        ArrayList<String> routePoints = new ArrayList<>();
        routePoints.add("Wielandtstraße 14\nHeidelberg");
        routePoints.add("Moltkestraße 21\nHeidelberg");
        routePoints.add("Roonstraße 7\nHeidelberg");

        return routePoints;
    }
}
