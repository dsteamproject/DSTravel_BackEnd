package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.entity.City;
import com.example.entity.TD;
import com.example.entity.Type;
import com.example.repository.CityRepository;
import com.example.repository.TDRepository;
import com.example.repository.TypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/worldcup")
public class WorldCupController {

    @Autowired
    TDRepository tdRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    TypeRepository typeRepository;

    @GetMapping(value = "/home", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> homeGET(@RequestParam("city") Integer city, @RequestParam("type") Integer type) {
        Map<String, Object> map = new HashMap<>();
        try {
            City city2 = cityRepository.findById(city).get();
            Type type2 = typeRepository.findById(type).get();
            List<TD> list = tdRepository.selectTDWorldCup(city2, type2);
            if (list.size() >= 2 && list.size() < 4) {
                List<TD> list1 = new ArrayList<>();
                int a[] = new int[list.size()];
                Random r = new Random();
                for (int i = 0; i < list.size(); i++) {
                    a[i] = r.nextInt(list.size()) + 1;
                    for (int j = 0; j < i; j++) {
                        if (a[i] == a[j]) {
                            i--;
                        }
                    }
                }
                for (int b = 0; b < 2; b++) {
                    list1.add(list.get(a[b] - 1));
                }
                map.put("randomlist", list1);
                map.put("status", 200);
            } else if (list.size() >= 4 && list.size() < 8) {
                List<TD> list1 = new ArrayList<>();
                int a[] = new int[list.size()];
                Random r = new Random();
                for (int i = 0; i < list.size(); i++) {
                    a[i] = r.nextInt(list.size()) + 1;
                    for (int j = 0; j < i; j++) {
                        if (a[i] == a[j]) {
                            i--;
                        }
                    }
                }
                for (int b = 0; b < 4; b++) {
                    list1.add(list.get(a[b] - 1));
                }
                map.put("randomlist", list1);
                map.put("status", 200);
            } else if (list.size() >= 8 && list.size() < 16) {
                List<TD> list1 = new ArrayList<>();
                int a[] = new int[list.size()];
                Random r = new Random();
                for (int i = 0; i < list.size(); i++) {
                    a[i] = r.nextInt(list.size()) + 1;
                    for (int j = 0; j < i; j++) {
                        if (a[i] == a[j]) {
                            i--;
                        }
                    }
                }
                for (int b = 0; b < 8; b++) {
                    list1.add(list.get(a[b] - 1));
                }
                map.put("randomlist", list1);
                map.put("status", 200);
            } else if (list.size() >= 16 && list.size() < 32) {
                List<TD> list1 = new ArrayList<>();
                int a[] = new int[list.size()];
                Random r = new Random();
                for (int i = 0; i < list.size(); i++) {
                    a[i] = r.nextInt(list.size()) + 1;
                    for (int j = 0; j < i; j++) {
                        if (a[i] == a[j]) {
                            i--;
                        }
                    }
                }
                for (int b = 0; b < 16; b++) {
                    list1.add(list.get(a[b] - 1));
                }
                map.put("randomlist", list1);
                map.put("status", 200);
            } else if (list.size() >= 32 && list.size() < 64) {
                List<TD> list1 = new ArrayList<>();
                int a[] = new int[list.size()];
                Random r = new Random();
                for (int i = 0; i < list.size(); i++) {
                    a[i] = r.nextInt(list.size()) + 1;
                    for (int j = 0; j < i; j++) {
                        if (a[i] == a[j]) {
                            i--;
                        }
                    }
                }
                for (int b = 0; b < 32; b++) {
                    list1.add(list.get(a[b] - 1));
                }
                map.put("randomlist", list1);
                map.put("status", 200);
            } else if (list.size() >= 64 && list.size() < 128) {
                List<TD> list1 = new ArrayList<>();
                int a[] = new int[list.size()];
                Random r = new Random();
                for (int i = 0; i < list.size(); i++) {
                    a[i] = r.nextInt(list.size()) + 1;
                    for (int j = 0; j < i; j++) {
                        if (a[i] == a[j]) {
                            i--;
                        }
                    }
                }
                for (int b = 0; b < 64; b++) {
                    list1.add(list.get(a[b] - 1));
                }
                map.put("randomlist", list1);
                map.put("status", 200);
            } else if (list.size() >= 128) {
                List<TD> list1 = new ArrayList<>();
                int a[] = new int[list.size()];
                Random r = new Random();
                for (int i = 0; i < list.size(); i++) {
                    a[i] = r.nextInt(list.size()) + 1;
                    for (int j = 0; j < i; j++) {
                        if (a[i] == a[j]) {
                            i--;
                        }
                    }
                }
                for (int b = 0; b < 128; b++) {
                    list1.add(list.get(a[b] - 1));
                }
                map.put("randomlist", list1);
                map.put("status", 200);
            } else {
                map.put("result", "자료가 2개 이상 필요합니다.");
            }

        } catch (Exception e) {
            map.put("status", e.hashCode());
        }
        return map;
    }
}
