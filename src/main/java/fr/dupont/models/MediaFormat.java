package fr.dupont.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public record MediaFormat(int height, int width) { }
