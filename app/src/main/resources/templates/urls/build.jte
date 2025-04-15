@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.BuildUrlPage

@param BuildUrlPage page

@template.layout.main(
title = "Add New URL",
page = page,
content = @`
    <h1>Track New Website</h1>

    @if(page.getErrors() != null)
        <div class="mb-3">
            <ul>
                @for(var validator : page.getErrors().values())
                    @for(var error : validator)
                        <li>${error.getMessage()}</li>
                    @endfor
                @endfor
            </ul>
        </div>
    @endif
    <div class="card mt-4">
        <div class="card-body">
            <form action="${NamedRoutes.urlsPath()}" method="POST">
                <div class="mb-3">
                    <label for="url" class="form-label">URL</label>
                    <input type="text"
                           id="url"
                           class="form-control"
                           name="url"
                           placeholder="https://example.com"
                           value="${page.getUrl()}"
                           required>
                </div>
                <button type="submit" class="btn btn-primary">Save URL</button>
            </form>
        </div>
    </div>
`
)
