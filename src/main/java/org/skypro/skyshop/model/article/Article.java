package org.skypro.skyshop.model.article;

import org.skypro.skyshop.model.search.Searchable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import java.util.UUID;

public class Article implements Searchable {
    private final UUID id;
    private String title;
    private String content;

    public Article(UUID id, String title, String content) {
        this.id = id;
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Заголовок статьи не может быть пустым");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Содержание статьи не может быть пустым");
        }
        this.title = title;
        this.content = content;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    @JsonIgnore
    public String getSearchTerm() {
        return title + " " + content;
    }

    @Override
    @JsonIgnore
    public String getContentType() {
        return "ARTICLE";
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    @JsonIgnore
    public String getStringRepresentation() {
        String shortContent = content.length() > 50 ? content.substring(0, 47) + "..." : content;
        return String.format("Article: %s (%s)", title, shortContent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}