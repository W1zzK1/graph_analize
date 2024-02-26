package me.gorbunov.dto;

public class GraphVertex {
    private Integer id;
    private Integer out;
    private Integer to;
    private Float weigth;

    public Float getWeigth() {
        return weigth;
    }

    public Integer getTo() {
        return to;
    }

    public Integer getOut() {
        return out;
    }

    @Override
    public String toString() {
        return "GraphVertex{" +
                "id=" + id +
                ", out=" + out +
                ", to=" + to +
                ", weigth=" + weigth +
                '}';
    }

    public GraphVertex(Integer id, Integer out, Integer to, Float weigth) {
        this.id = id;
        this.out = out;
        this.to = to;
        this.weigth = weigth;
    }
}
