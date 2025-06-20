package com.example.moviesearcher.service;

import com.example.moviesearcher.dto.CrewMemberDTO;
import com.example.moviesearcher.dto.GenreDTO;
import com.example.moviesearcher.entity.CrewMember;
import com.example.moviesearcher.entity.Genre;

import static com.example.moviesearcher.mapper.CrewMemberMapper.CREW_MEMBER_MAPPER;
import static com.example.moviesearcher.mapper.GenreMapper.GENRE_MAPPER;
import com.example.moviesearcher.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    @Transactional
    public GenreDTO saveGenre(GenreDTO genreDTO) {
        if (genreRepository.findByName(genreDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Genre with name '" + genreDTO.getName() + "' already exists");
        }

        Genre genre = GENRE_MAPPER.genreDTOToGenre(genreDTO);
        genre = genreRepository.save(genre);
        return GENRE_MAPPER.genreToGenreDTO(genre);
    }

    public List<GenreDTO> findAllGenres() {
        return genreRepository.findAll().stream()
                .map(GENRE_MAPPER::genreToGenreDTO)
                .collect(Collectors.toList());
    }

    public PagedModel<GenreDTO> findPagedGenres(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Genre> genrePage = genreRepository.findAll(pageable);
        Page<GenreDTO> genreDTOPage = genrePage.map(GENRE_MAPPER::genreToGenreDTO);
        return new PagedModel<>(genreDTOPage);
    }

    public GenreDTO findGenreById(Byte id) {
        return genreRepository.findById(id)
                .map(GENRE_MAPPER::genreToGenreDTO)
                .orElse(null);
    }

    public GenreDTO findGenreByName(String name) {
        return genreRepository.findByName(name)
                .map(GENRE_MAPPER::genreToGenreDTO)
                .orElse(null);
    }

    @Transactional
    public void deleteGenre(Byte id) {
        if (!genreRepository.existsById(id)) {
            throw new IllegalArgumentException("Genre not found with ID: " + id);
        }
        genreRepository.deleteById(id);
    }

    @Transactional
    public GenreDTO updateGenre(GenreDTO genreDTO) {
        Genre genreFromDb = genreRepository.findById(genreDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Genre not found with ID: " + genreDTO.getId()));

        if (genreDTO.getName() != null && !genreDTO.getName().equals(genreFromDb.getName())) {
            if (genreRepository.findByName(genreDTO.getName()).isPresent()) {
                throw new IllegalArgumentException("Genre with name '" + genreDTO.getName() + "' already exists");
            }
            genreFromDb.setName(genreDTO.getName());
        }

        genreFromDb = genreRepository.save(genreFromDb);
        return GENRE_MAPPER.genreToGenreDTO(genreFromDb);
    }
}